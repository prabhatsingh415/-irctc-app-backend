package org.example.Servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.Utilities;
import org.example.services.TicketServices;
import org.example.services.TrainServices;

import java.io.IOException;

public class BookTicketServlet extends HttpServlet {
    private final TrainServices trainServices = new TrainServices();
    private final TicketServices ticketServices = new TicketServices();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();

        String step = req.getParameter("step");  // Step to determine the stage of input
        String jsonResponse;

        if ("trainId".equals(step)) {
            // Step 1: Validate Train ID
            int trainId = Integer.parseInt(req.getParameter("trainId"));

            if (trainServices.isTrainIdValid(trainId)) {
                session.setAttribute("trainId", trainId);
                jsonResponse = "{\"success\": true, \"message\": \"Train ID validated. Please enter the travel date.\"}";
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
            String email = (String) session.getAttribute("userEmail"); // Assuming email is stored in session
            int trainId = (int) session.getAttribute("trainId");
            String dateOfTravel = (String) session.getAttribute("dateOfTravel");

            ticketServices.bookTicket(trainId, "Destination", "Origin", dateOfTravel, name, email);

            jsonResponse = "{\"success\": true, \"message\": \"Ticket booked successfully!\"}";
            resp.getWriter().write(jsonResponse);
        }
    }
}
