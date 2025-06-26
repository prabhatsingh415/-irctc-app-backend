package org.example.services;

import org.example.utilities.TicketGenerator;
import org.example.utilities.Utilities;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.example.utilities.Utilities.generateUniqueId;
import static org.example.database.DataBaseConfig.createConnection;

public class TicketServices {

    // Method to book a ticket
    public void bookTicket(int trainId, String destination, String arrival, String date, String name, String email) {
        String query = "INSERT INTO ticket (TicketId, TicketDate, PassangerName, DateOfTravel, TrainID, Destination, Departure) VALUES (?, ?, ?, ?, ?, ?, ?)";//query for creating ticket
        String query2 = "UPDATE traindetails SET TotalSeats = TotalSeats - 1 WHERE TrainID = ?"; // query to update the seats in train
        String query3 = "SELECT TrainType, ArrivalTime, DepartureTime FROM traindetails WHERE TrainID = ?"; // query for selecting the train details

        try (Connection connection = createConnection()) {

            String trainType;
            Time arrivalTime;
            Time departureTime;

            // Fetch train details
            try (PreparedStatement stmt = connection.prepareStatement(query3)) {
                stmt.setInt(1, trainId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        trainType = rs.getString("TrainType");
                        arrivalTime = rs.getTime("ArrivalTime");
                        departureTime = rs.getTime("DepartureTime");
                    } else {
                        System.out.println("No train found for Train ID: " + trainId);
                        return;
                    }
                }
            }

            // Prepare to insert ticket and update seat count
            try (PreparedStatement psInsert = connection.prepareStatement(query);
                 PreparedStatement psUpdate = connection.prepareStatement(query2)) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date sqlTravelDate = new Date(sdf.parse(date).getTime());
                String ticketID = generateUniqueId();

                psInsert.setString(1, ticketID);
                psInsert.setDate(2, new Date(System.currentTimeMillis())); // Current date for TicketDate
                psInsert.setString(3, name);
                psInsert.setDate(4, sqlTravelDate);
                psInsert.setInt(5, trainId);
                psInsert.setString(6, destination);
                psInsert.setString(7, arrival);
                psInsert.executeUpdate();

                psUpdate.setInt(1, trainId);
                psUpdate.executeUpdate();
                // Generate and send ticket email
                TicketGenerator ticket = new TicketGenerator();
                String path = ticket.printTicket(ticketID, trainId, trainType, arrivalTime, departureTime, name, arrival, destination, sqlTravelDate);

                VerificationEmail ticketEmail = new VerificationEmail();
                if (ticketEmail.sendTicket(email, name, path)) {
                    System.out.println("The ticket has been sent to your email address.");
                    Utilities.deleteTicket();
                } else {
                    System.out.println("Something went wrong while sending the ticket. Please try again.");
                }
            }

        } catch (SQLException | ParseException e) {
            System.err.println("Error booking ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to cancel a ticket by its TicketId
    public boolean cancelTicket(String ticketId) {
        String query = "DELETE FROM ticket WHERE TicketId = ?";

        try (Connection connection = createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ticketId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error: There was an issue while canceling the ticket. Please try again.");
            throw new RuntimeException(e);
        }
    }

    // Method to check if a ticket ID is valid (exists in DB)
    public boolean isTicketIdValid(String ticketId) {
        String query = "SELECT TrainID FROM ticket WHERE TicketId = ?";

        try (Connection connection = createConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {

            return false;
        }
    }
}