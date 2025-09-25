package org.example.services;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;

import java.io.IOException;
import java.nio.file.Paths;

public class EmailSender {

    private final String fromEmail;
    private final String mailerSendApiKey;

    public EmailSender(String fromEmail) {
        this.fromEmail = fromEmail;
        this.mailerSendApiKey = System.getenv("MAILERSEND_API_KEY");

        if (this.mailerSendApiKey == null) {
            throw new RuntimeException("MAILERSEND_API_KEY not found! Please check your environment variables.");
        }
    }

    public boolean sendEmail(String toName, String toEmail, String subject, String message, String filePath) {
        try {
            MailerSend mailerSend = new MailerSend();
            mailerSend.setToken(mailerSendApiKey);

            Email email = new Email();
            email.setFrom("IRCTC Bot", fromEmail);

            // Recipient
            email.addRecipient(toName, toEmail);

            // Subject & Message
            email.setSubject(subject);
            email.setPlain(message);

            // Reply-To
            email.AddReplyTo("IRCTC Support", fromEmail);

            // Attachment
            if (filePath != null && !filePath.isEmpty()) {
                email.attachFile(Paths.get(filePath).toString());
            }

            // Send Email
            mailerSend.emails().send(email);
            System.out.println("Email sent successfully!");
            return true;

        } catch (MailerSendException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
}