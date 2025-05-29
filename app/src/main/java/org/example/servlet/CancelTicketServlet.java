package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.services.TicketServices;

import java.io.IOException;

public class CancelTicketServlet extends HttpServlet {
    TicketServices ticketServices = new TicketServices();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String ticketId = req.getParameter("ticketId");
        if(ticketServices.isTicketIdValid(ticketId)){
             if(ticketServices.cancelTicket(ticketId)){
                 resp.getWriter().write("{\"success\": true, \"message\": \"Ticket successfully canceled\"}");
             }else{
                 resp.getWriter().write("{\"success\": false, \"message\": \"Error: There was an issue while canceling the ticket. Please try again.\"}");
             }
        }else{
            resp.getWriter().write("{\"success\": false, \"message\": \"Invalid TicketId\"}");
        }
    }
}
