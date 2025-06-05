package org.example.services;

import static org.example.database.DataBaseConfig.createConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class VerificationEmail {
    int code = code(); // Generates a random verification code when the object is created

    String from = System.getenv("EMAIL_USERNAME");  // Loads the sender's email from the environment variables

    // Method to send the verification email to the user
    public boolean sendEmail(String mail, String userName) {
        String subject = "Verification E-Mail";
        String message = "Dear " + userName +
                ",\n\nWelcome to IRCTC Ticket Booking App! To complete your sign-up process, we just need to verify your email address. Here is your verification code: **" +
                code +
                "**.\n\nPlease enter this code in the signup form to confirm your email address and complete your registration. If you did not request this signup or believe this message was sent in error, please disregard this email.\n\n" +
                "Thank you for joining us!\n" +
                "Best regards,\n" +
                "The IRCTC - Ticket Booking App Team\n" +
                "Email :- singh.prabhat.work@gmail.com\n";

        EmailSender emailSender = new EmailSender(from);
        boolean success = emailSender.sendEmail(mail, subject, message, null);

        if (success) {
            System.out.println("Verification email sent successfully!");
        } else {
            System.err.println("Error: We couldn't send the verification email. Please check your email settings and try again.");
        }
        return success;
    }

    // Method to send the ticket confirmation email with attachment
    public boolean sendTicket(String mail, String user, String path) {
        String subject = "Your Ticket Confirmation";
        String message = "Dear " + user +
                "\n" +
                "Thank you for choosing our services. Please find your ticket details below:\n" +
                "\n" +
                "Your ticket is attached to this email. Kindly carry a printout or an electronic copy of it during your journey.\n" +
                "\n" +
                "If you have any questions or require further assistance, feel free to reach out to us.\n" +
                "\n" +
                "Safe travels!\n" +
                "\n" +
                "Best regards,\n" +
                "Prabhat Singh\n" +
                "contact :- singh.prabhat.work@gmail.com\n" +
                "IRCTC-TICKET_BOOKING_APP\n";

        EmailSender emailSender = new EmailSender(from);
        boolean success = emailSender.sendEmail(mail, subject, message, path);

        if (success) {
            System.out.println("Ticket confirmation email sent successfully!");
        } else {
            System.err.println("Error: We couldn't send the ticket confirmation email. Please try again.");
        }
        return success;
    }

    // Generate a random 4-digit verification code
    private int code() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // 1000-9999
    }

    // Check if user input matches generated code
    public boolean authenticator(int userInput) {
        return userInput == code;
    }

    // Check if email is available (not used by any user)
    public boolean isEmailAvailable(String email) {
        String query = "SELECT EXISTS (SELECT 1 FROM user WHERE userEMAIL = ?)";

        try (Connection connection = createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Return true if email does NOT exist (available), false otherwise
                    return !resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while checking email availability. Please contact support or try again later.");
        }
        return false;
    }
}