package org.example.services;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Random;

public class VerificationEmail {
      int code = code();

    Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\acer\\Irctc\\app\\.env").load();
    String from = dotenv.get("EMAIL_USERNAME");
    public boolean sendEmail(String mail, String userName){


        String subject = "Verification E-Mail";
        String message = "Dear " + userName +
                ",\n\nWelcome to IRCTC Ticket Booking App! To complete your sign-up process, we just need to verify your email address. Here is your verification code: **" +
              code  +
                "**.\n\nPlease enter this code in the signup form to confirm your email address and complete your registration. If you did not request this signup or believe this message was sent in error, please disregard this email.\n\n" +
                "Thank you for joining us!\n" +
                "Best regards,\n" +
                "The IRCTC - Ticket Booking App Team\n" +
                "Email :- singh.prabhat.work@gmail.com\n";

        EmailSender emailSender = new EmailSender(from);

        return emailSender.sendEmail(mail, subject, message, null);
    }

    public boolean sendTicket(String mail,String user,String path){
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
                 "contact :- singh.prabhat.work@gmail.com" +
                "IRCTC-TICKET_BOOKING_APP\n";

        EmailSender emailSender = new EmailSender(from);

        // Send the email with the attachment
       return emailSender.sendEmail(mail, subject, message,path);
    }


    private  int code(){
        Random random = new Random();
       return  1000 + random.nextInt(9000); // Generates a number between 1000 and 9999
    }

    public boolean authenticator(int userInput){
      return userInput == code;
    }
}