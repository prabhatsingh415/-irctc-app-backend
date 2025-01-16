package org.example.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.dataBase.DataBaseConfig;
import static org.example.dataBase.DataBaseConfig.createConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


public class VerificationEmail {
    int code = code(); // Generates a random verification code when the object is created

    Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\acer\\Irctc\\app\\.env").load();
    String from = dotenv.get("EMAIL_USERNAME");  // Loads the sender's email from the environment variables

    // Method to send the verification email to the user
    public boolean sendEmail(String mail, String userName) {
        // Email subject and message content with dynamic userName and verification code
        String subject = "Verification E-Mail";
        String message = "Dear " + userName +
                ",\n\nWelcome to IRCTC Ticket Booking App! To complete your sign-up process, we just need to verify your email address. Here is your verification code: **" +
                code +
                "**.\n\nPlease enter this code in the signup form to confirm your email address and complete your registration. If you did not request this signup or believe this message was sent in error, please disregard this email.\n\n" +
                "Thank you for joining us!\n" +
                "Best regards,\n" +
                "The IRCTC - Ticket Booking App Team\n" +
                "Email :- singh.prabhat.work@gmail.com\n";

        // Create an instance of EmailSender to send the email
        EmailSender emailSender = new EmailSender(from);

        // Attempt to send the email and return the result
        boolean success = emailSender.sendEmail(mail, subject, message, null);

        // Check if email was sent successfully and display the appropriate message
        if (success) {
            System.out.println("Verification email sent successfully!");
        } else {
            System.err.println("Error: We couldn't send the verification email. Please check your email settings and try again.");
        }

        return success; // Return the result of email sending
    }

    // Method to send the ticket confirmation email to the user with the attached ticket
    public boolean sendTicket(String mail, String user, String path) {
        // Email subject and message content with dynamic userName
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

        // Create an instance of EmailSender to send the ticket email
        EmailSender emailSender = new EmailSender(from);

        // Attempt to send the email with the ticket attachment and return the result
        boolean success = emailSender.sendEmail(mail, subject, message, path);

        // Check if email was sent successfully and display the appropriate message
        if (success) {
            System.out.println("Ticket confirmation email sent successfully!");
        } else {
            System.err.println("Error: We couldn't send the ticket confirmation email. Please try again.");
        }

        return success; // Return the result of email sending
    }

    // Method to generate a random 4-digit verification code
    private int code() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Generates a number between 1000 and 9999
    }

    // Method to authenticate the user by comparing the user input with the generated code
    public boolean authenticator(int userInput) {
        return userInput == code; // Check if user input matches the generated code
    }

    public boolean isEmailAvailable(String email){
        try(Connection connection = createConnection()){
            String query = "SELECT EXISTS (\n" +
                    "    SELECT 1 \n" +
                    "    FROM user\n" +
                    "    WHERE userEMAIL = ?\n" +
                    ");\n";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Return true if the email is NOT available (does not exist), otherwise false
                return !resultSet.getBoolean(1); // Flip the result
            }
        }catch (SQLException e) {
            System.out.println("An error occurred while checking email availability. Please contact support or try again later.");
        }
        return false;
    }
}
