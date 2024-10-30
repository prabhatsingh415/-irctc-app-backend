package org.example.services;

import io.github.cdimascio.dotenv.Dotenv;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;

public class VerificationEmail {
      int code = code();

    Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\acer\\Irctc\\app\\.env").load();

    public boolean sendEmail(String mail, String userName){

        String from = dotenv.get("EMAIL_USERNAME");
        String subject = "Verification E-Mail";
        String message = "Dear " + userName +
                ",\n\nWelcome to IRCTC Ticket Booking App! To complete your sign-up process, we just need to verify your email address. Here is your verification code: **" +
              code  +
                "**.\n\nPlease enter this code in the signup form to confirm your email address and complete your registration. If you did not request this signup or believe this message was sent in error, please disregard this email.\n\n" +
                "Thank you for joining us!\n" +
                "Best regards,\n" +
                "The IRCTC - Ticket Booking App Team\n" +
                "Email :- v60871189@gmail.com \n";
        return send(from, mail, subject, message);
    }

    private boolean send(String from, String to, String subject, String message) {
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        //App password
        final String  appPassword = dotenv.get("EMAIL_APP_PASSWORD");
        //Session object
      Session session =  Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,appPassword);
            }
        });
        session.setDebug(true);

        MimeMessage message1 = new MimeMessage(session);


        try {
            //Sender
            message1.setFrom(new InternetAddress(from));

            //receiver
            message1.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

             //Set Subject
            message1.setSubject(subject);
            //Message body
            message1.setText(message);

            //Send Email
            Transport.send(message1);

            System.out.println("Email sent successfully!");

            return true;
        } catch (MessagingException e) {
            System.out.println("Error: Something went wrong!");
            e.printStackTrace();
        }
      return false;
    }

    private  int code(){
        Random random = new Random();
       return  1000 + random.nextInt(9000); // Generates a number between 1000 and 9999
    }

    public boolean authenticator(int userInput){
      return userInput == code;
    }
}