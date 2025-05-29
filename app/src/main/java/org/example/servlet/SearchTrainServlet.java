package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.services.TrainServices;


import java.io.IOException;


public class SearchTrainServlet extends HttpServlet {
    TrainServices trainServices = new TrainServices();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String arrivalStation = req.getParameter("arrivalStation");
        String destinationStation = req.getParameter("destinationStation");

        String data  =  trainServices.searchTrain(arrivalStation,destinationStation);
        resp.getWriter().write(data);
    }
}
