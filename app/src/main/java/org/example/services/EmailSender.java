package org.example.services;
import jakarta.activation.DataHandler;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;

public class EmailSender {

    private final String host = "smtp.gmail.com";
    private final String fromEmail;
    private final String appPassword;

    // Constructor to initialize sender email and app password from the .env file
    public EmailSender(String fromEmail) {
        this.fromEmail = fromEmail;
        Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\acer\\Irctc\\app\\.env").load();
        this.appPassword = dotenv.get("EMAIL_APP_PASSWORD");
        if (this.appPassword == null) {
            throw new RuntimeException("Something Went Wrong,Please Try Again Letter !");
        }
    }

    // Method to configure session properties
    private Session configureSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });
    }

    // Reusable method to send an email with an optional attachment
    public boolean sendEmail(String to, String subject, String message, String filePath) {
        try {
            // Configure session
            Session session = configureSession();
            session.setDebug(true);

            // Create the email message
            MimeMessage mimeMessage = new MimeMessage(session);

            // Set sender and recipient
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set subject
            mimeMessage.setSubject(subject);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Add the text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message);
            multipart.addBodyPart(textPart);

            // Add attachment if the file path is provided
            if (filePath != null && !filePath.isEmpty()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(new File(filePath).getName());
                multipart.addBodyPart(attachmentPart);
            }

            // Set the content to the email message
            mimeMessage.setContent(multipart);

            // Send the email
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully!");
            return true;

        } catch (MessagingException e) {
            System.err.println("Error: Could not send email.");
            e.printStackTrace();
        }
        return false;
    }
}
