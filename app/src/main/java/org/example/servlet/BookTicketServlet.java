package org.example.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.utilities.Utilities;
import org.example.services.TicketServices;
import org.example.services.TrainServices;

import java.io.IOException;
import java.util.List;

public class BookTicketServlet extends HttpServlet {
    private final TrainServices trainServices = new TrainServices();
    private final TicketServices ticketServices = new TicketServices();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false); // No new session

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Check if user is logged in
        if (session == null || session.getAttribute("userEmail") == null || session.getAttribute("isAuthenticated") == null) {
            resp.getWriter().write("{\"success\": false, \"message\": \"User not logged in. Please login first.\"}");
            return;
        }

        String step = req.getParameter("step"); // Step to determine the stage of input
        String jsonResponse;

        if ("trainId".equals(step)) {
            // Step 1: Validate Train ID
            int trainId;
            try {
                trainId = Integer.parseInt(req.getParameter("trainId"));
            } catch (NumberFormatException e) {
                jsonResponse = "{\"success\": false, \"message\": \"Invalid Train ID format.\"}";
                resp.getWriter().write(jsonResponse);
                return;
            }

            if (trainServices.isTrainIdValid(trainId)) {
                session.setAttribute("trainId", trainId);

                List<String> stations = trainServices.getStationsForTrain(trainId);

                // Convert to JSON array string
                String stationsJson = new com.google.gson.Gson().toJson(stations);

                jsonResponse = "{\"success\": true, " +
                        "\"message\": \"Train ID validated. Please enter the travel date.\"," +
                        "\"stations\": " + stationsJson + "}";

            } else {
                jsonResponse = "{\"success\": false, \"message\": \"Invalid Train ID. Please enter a valid Train ID.\"}";
            }
            resp.getWriter().write(jsonResponse);
            return;
        }

        if ("date".equals(step)) {
            // Step 2: Validate Travel Date
            String dateOfTravel = req.getParameter("date");

            if (Utilities.isDateValid(dateOfTravel)) {
                session.setAttribute("dateOfTravel", dateOfTravel);
                jsonResponse = "{\"success\": true, \"message\": \"Date validated. Please enter passenger name.\"}";
            } else {
                jsonResponse = "{\"success\": false, \"message\": \"Invalid Date. Please enter a valid date (today or later).\"}";
            }
            resp.getWriter().write(jsonResponse);
            return;
        }

        if ("passengerName".equals(step)) {
            // Step 3: Get Passenger Name and Book Ticket
            String name = req.getParameter("name");
            String source = req.getParameter("src");
            String destination = req.getParameter("dest");

            if (name == null || source == null || destination == null ||
                    name.trim().isEmpty() || source.trim().isEmpty() || destination.trim().isEmpty()) {
                jsonResponse = "{\"success\": false, \"message\": \"All fields (name, source, destination) are required.\"}";
                resp.getWriter().write(jsonResponse);
                return;
            }

            String email = (String) session.getAttribute("userEmail");
            int trainId = (int) session.getAttribute("trainId");
            String dateOfTravel = (String) session.getAttribute("dateOfTravel");

            try {
                ticketServices.bookTicket(trainId, source, destination, dateOfTravel, name, email);
                jsonResponse = "{\"success\": true, \"message\": \"Ticket booked successfully and sent to your email!\"}";
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                jsonResponse = "{\"success\": false, \"message\": \"Error booking ticket. Please try again.\"}";
            }
            resp.getWriter().write(jsonResponse);
        }
    }
}