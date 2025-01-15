package org.example.services;


import org.example.TicketGenerator;
import org.example.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.UUID;

import static org.example.dataBase.DataBaseConfig.createConnection;

public class TicketServices {

    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public void bookTicket(int trainId, String destination, String arrival, String date, String name,String email) {
        String query = "INSERT INTO ticket (TicketId, TicketDate, PassangerName, DateOfTravel, TrainID, Destination, Departure) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String query2 = "UPDATE TrainDetails SET TotalSeats = TotalSeats - 1\n  WHERE trainId = ?;";
        try (Connection connection = createConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);


            // Convert input travelDate to java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date parsedTravelDate = sdf.parse(date);
            Date sqlTravelDate = new Date(parsedTravelDate.getTime());

            // Set parameters in the correct order
            preparedStatement.setString(1, generateUniqueId()); // TicketId
            preparedStatement.setDate(2, new Date(System.currentTimeMillis())); // Current date for TicketDate
            preparedStatement.setString(3, name); // Passenger Name
            preparedStatement.setDate(4, sqlTravelDate); // Date of Travel
            preparedStatement.setInt(5, trainId); // TrainID
            preparedStatement.setString(6, destination); // Destination
            preparedStatement.setString(7, arrival); // Departure

            // Execute the query
            preparedStatement.executeUpdate();
            //Decrement seat count
            PreparedStatement preparedStatement1 = connection.prepareStatement(query2);
            preparedStatement1.setInt(1, trainId);
            preparedStatement1.executeUpdate();
            System.out.println("Ticket booked successfully!");
            TicketGenerator ticket = new TicketGenerator();
            String  path  = ticket.printTicket(trainId,name,arrival,destination);
            VerificationEmail TicketEmail = new VerificationEmail();
            if (TicketEmail.sendTicket(email,name,path)) {
                System.out.println("It is sent on your E-Mail address");
                Utilities.deleteTicket();
            }else{
                System.out.println("Something Went Wrong !");
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean cancelTicket(String ticketId) {
        String query = "DELETE FROM ticket WHERE ticketId = ?";

        try (Connection connection = createConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,ticketId);
            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
              return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}