package org.example.services;

import jakarta.servlet.http.HttpSession;
import static org.example.database.DataBaseConfig.createConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class VerificationEmail {
    private final String from;

    public VerificationEmail() {
        this.from = System.getenv("EMAIL_USERNAME"); // Loads the sender's email from environment variables
        if (this.from == null) {
            throw new RuntimeException("EMAIL_USERNAME not found! Please check your environment variables.");
        }
    }

    // Method to send the verification email to the user
    public boolean sendEmail(String toEmail, String userName, HttpSession session) {
        int code = code(); // Generate a new code for each email
        session.setAttribute("verificationCode", code); // Store code in session

        String subject = "Verification E-Mail";
        String message = "Dear " + userName +
                ",\n\nWelcome to IRCTC Ticket Booking App! To complete your sign-up process, we just need to verify your email address. Here is your verification code: **" +
                code +
                "**.\n\nPlease enter this code in the signup form to confirm your email address and complete your registration. If you did not request this signup or believe this message was sent in error, please disregard this email.\n\n" +
                "Thank you for joining us!\n" +
                "Best regards,\n" +
                "The IRCTC - Ticket Booking App Team\n" +
                "Email: singh.prabhat.work@gmail.com\n";

        EmailSender emailSender = new EmailSender(from);
        boolean success = emailSender.sendEmail(userName, toEmail, subject, message, null);

        if (success) {
            System.out.println("Verification email sent successfully with code: " + code);
        } else {
            System.err.println("Error: Failed to send verification email to " + toEmail + ". Please check your email settings and try again.");
        }
        return success;
    }

    // Method to send the ticket confirmation email with attachment
    public boolean sendTicket(String toEmail, String userName, String filePath) {
        String subject = "Your Ticket Confirmation";
        String message = "Dear " + userName + ",\n\n" +
                "Thank you for choosing our services. Please find your ticket details below:\n\n" +
                "Your ticket is attached to this email. Kindly carry a printout or an electronic copy of it during your journey.\n\n" +
                "If you have any questions or require further assistance, feel free to reach out to us.\n\n" +
                "Safe travels!\n\n" +
                "Best regards,\n" +
                "Prabhat Singh\n" +
                "Contact: singh.prabhat.work@gmail.com\n" +
                "IRCTC - Ticket Booking App\n";

        EmailSender emailSender = new EmailSender(from);
        boolean success = emailSender.sendEmail(userName, toEmail, subject, message, filePath);

        if (success) {
            System.out.println("Ticket confirmation email sent successfully to " + toEmail);
        } else {
            System.err.println("Error: Failed to send ticket confirmation email to " + toEmail + ". Please verify the file path and try again.");
        }
        return success;
    }

    // Generate a random 4-digit verification code
    private int code() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // 1000-9999
    }

    // Check if user input matches the code stored in session
    public boolean authenticator(int userInput, HttpSession session) {
        Integer storedCode = (Integer) session.getAttribute("verificationCode");
        if (storedCode == null) {
            System.err.println("No verification code found in session for session ID: " + session.getId());
            return false;
        }
        boolean isValid = userInput == storedCode;
        System.out.println("Retrieved OTP: " + storedCode + ", User Input: " + userInput + ", Session ID: " + session.getId() + ", Valid: " + isValid);
        if (isValid) {
            session.removeAttribute("verificationCode"); // Clean up session
        }
        return isValid;
    }

    // Check if email is available
    public boolean isEmailAvailable(String email) {
        String query = "SELECT EXISTS (SELECT 1 FROM user WHERE userEMAIL = ?)";
        try (Connection connection = createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    boolean exists = resultSet.getBoolean(1);
                    System.out.println("Email availability check for " + email + ": " + (exists ? "taken" : "available"));
                    return !exists; // Return true if email does NOT exist (available)
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while checking email availability for " + email + ": " + e.getMessage());
            return false; // Return false on error to prevent registration with unverified email
        }
        return false;
    }
}