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

    private final String fromEmail;  // Sender's email address
    private final String appPassword; // App password for authentication

    // Constructor to initialize sender email and app password from the .env file
    public EmailSender(String fromEmail) {
        this.fromEmail = fromEmail;
        Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\acer\\Irctc\\app\\.env").load();

        // Retrieve the app password from the .env file
        this.appPassword = dotenv.get("EMAIL_APP_PASSWORD");

        // Throw an exception if app password is not found
        if (this.appPassword == null) {
            throw new RuntimeException("Something Went Wrong,Please Try Again Later!");
        }
    }

    // Method to configure session properties for sending emails
    private Session configureSession() {
        Properties properties = new Properties();
        String host = "smtp.gmail.com";  // SMTP server for Gmail

        // Set the properties for connecting to the SMTP server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");  // SSL port for Gmail
        properties.put("mail.smtp.ssl.enable", "true");  // Enable SSL
        properties.put("mail.smtp.auth", "true");  // Enable authentication

        // Return a session object with authentication credentials
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Return the email and app password for authentication
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });
    }

    // Reusable method to send an email with an optional attachment
    public boolean sendEmail(String to, String subject, String message, String filePath) {
        try {
            // Configure session with the necessary properties and credentials
            Session session = configureSession();
            session.setDebug(true);  // Enable debug output for troubleshooting

            // Create a new email message
            MimeMessage mimeMessage = new MimeMessage(session);

            // Set the sender's and recipient's email addresses
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set the subject of the email
            mimeMessage.setSubject(subject);

            // Create a multipart message to include both text and attachments
            Multipart multipart = new MimeMultipart();

            // Create a body part for the email text and add it to the multipart message
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message);
            multipart.addBodyPart(textPart);

            // Add attachment if the file path is provided
            if (filePath != null && !filePath.isEmpty()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);  // Create a data source for the file
                attachmentPart.setDataHandler(new DataHandler(source));  // Attach the file to the email
                attachmentPart.setFileName(new File(filePath).getName());  // Set the file name for the attachment
                multipart.addBodyPart(attachmentPart);  // Add the attachment part to the multipart
            }

            // Set the content of the email to the multipart message (text + attachment)
            mimeMessage.setContent(multipart);

            // Send the email via the transport service
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully!");
            return true;

        } catch (MessagingException e) {
            // User-friendly error message instead of stack trace
            System.err.println("Oops! Something went wrong while sending the email. Please check your internet connection or email settings, and try again.");
        }
        return false;  // Return false if sending the email fails
    }
}

