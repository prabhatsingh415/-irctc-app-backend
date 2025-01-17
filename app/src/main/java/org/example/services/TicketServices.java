package org.example.services;

import org.example.TicketGenerator;
import org.example.Utilities;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static org.example.dataBase.DataBaseConfig.createConnection;

public class TicketServices {

    // Method to generate a unique TicketId
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // Example: 1F2B3C4D
    }

    // Method to book a ticket
    public void bookTicket(int trainId, String destination, String arrival, String date, String name, String email) {
        // SQL queries for inserting a ticket, updating train seat availability, and retrieving train details
        String query = "INSERT INTO ticket (TicketId, TicketDate, PassangerName, DateOfTravel, TrainID, Destination, Departure) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String query2 = "UPDATE TrainDetails SET TotalSeats = TotalSeats - 1 WHERE trainId = ?;";
        String query3 = "SELECT TrainType, ArrivalTime, DepartureTime FROM TrainDetails WHERE TrainID = ?";  // New query to retrieve train details

        try (Connection connection = createConnection()) {
            // 1. Get train details (TrainType, ArrivalTime, DepartureTime)
            PreparedStatement stmt = connection.prepareStatement(query3);
            stmt.setInt(1, trainId); // Set the trainId parameter
            ResultSet rs = stmt.executeQuery();

            // Declare variables to store train details
            String trainType;
            Time arrivalTime;
            Time departureTime;

            // Fetch the details from the result set
            if (rs.next()) {
                trainType = rs.getString("TrainType");
                arrivalTime = rs.getTime("ArrivalTime");
                departureTime = rs.getTime("DepartureTime");
            } else {
                System.out.println("No train found for Train ID: " + trainId);
                return;  // Exit the method if no train found with the given TrainID
            }

            // 2. Proceed with booking the ticket (Insert into ticket table and update seats)
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Convert input travelDate to java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date parsedTravelDate = sdf.parse(date);
            Date sqlTravelDate = new Date(parsedTravelDate.getTime());
            String ticketID = generateUniqueId();

            // Set parameters in the correct order
            preparedStatement.setString(1, ticketID); // TicketId
            preparedStatement.setDate(2, new Date(System.currentTimeMillis())); // Current date for TicketDate
            preparedStatement.setString(3, name); // Passenger Name
            preparedStatement.setDate(4, sqlTravelDate); // Date of Travel
            preparedStatement.setInt(5, trainId); // TrainID
            preparedStatement.setString(6, destination); // Destination
            preparedStatement.setString(7, arrival); // Departure

            // Execute the query to insert the ticket
            preparedStatement.executeUpdate();

            // Decrement seat count for the train
            PreparedStatement preparedStatement1 = connection.prepareStatement(query2);
            preparedStatement1.setInt(1, trainId);
            preparedStatement1.executeUpdate();

            // Success message for ticket booking
            System.out.println("Ticket booked successfully!");

            // Generate and send the ticket
            TicketGenerator ticket = new TicketGenerator();
            String path = ticket.printTicket(ticketID, trainId, trainType,arrivalTime,departureTime,name, arrival, destination,sqlTravelDate);

            VerificationEmail ticketEmail = new VerificationEmail();

            // Try to send the ticket via email
            if (ticketEmail.sendTicket(email, name, path)) {
                // Success message for email sending
                System.out.println("The ticket has been sent to your email address.");
                Utilities.deleteTicket(); // Delete the ticket after sending email
            } else {
                // Error message if email sending fails
                System.out.println("Something went wrong while sending the ticket. Please try again.");
            }

        } catch (SQLException | ParseException e) {
            System.err.println("Error: There was an issue while booking the ticket. Please check your input or database connection.");
        }
    }

    // Method to cancel a ticket by its TicketId
    public boolean cancelTicket(String ticketId) {
        String query = "DELETE FROM ticket WHERE ticketId = ?";

        try (Connection connection = createConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ticketId);

            // Execute the query to delete the ticket
            int rowsAffected = preparedStatement.executeUpdate();

            // If the ticket is deleted successfully, return true (canceled)
            if (rowsAffected > 0) {
                return true; // Ticket successfully canceled
            }

        } catch (SQLException e) {
            // Error handling with user-friendly message
            System.err.println("Error: There was an issue while canceling the ticket. Please try again.");
            throw new RuntimeException(e);
        }
        // Return false if no ticket was found
        return false;
    }

}
