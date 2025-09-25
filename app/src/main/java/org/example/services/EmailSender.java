package org.example.services;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class EmailSender {

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);
    private final String fromEmail;
    private final String mailerSendApiKey;

    public EmailSender(String fromEmail) {
        this.fromEmail = fromEmail;
        this.mailerSendApiKey = System.getenv("MAILERSEND_API_KEY");

        if (this.mailerSendApiKey == null || this.mailerSendApiKey.trim().isEmpty()) {
            log.error("MAILERSEND_API_KEY is missing or empty");
            throw new RuntimeException("MAILERSEND_API_KEY not found! Please check your environment variables.");
        }
        if (this.fromEmail == null || this.fromEmail.trim().isEmpty()) {
            log.error("From email is missing or empty");
            throw new RuntimeException("EMAIL_USERNAME not found! Please check your environment variables.");
        }
    }

    public boolean sendEmail(String toName, String toEmail, String subject, String message, String filePath) {
        log.info("Attempting to send email to: {}", toEmail); // Better logging for debugging
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

            // Reply-To (Fixed: lowercase 'add' - SDK method is case-sensitive)
            email.AddReplyTo("IRCTC Support", fromEmail);

            // Attachment
            if (filePath != null && !filePath.isEmpty()) {
                log.debug("Attaching file: {}", filePath);
                email.attachFile(Paths.get(filePath).toString());
            }

            // Send Email (uses HTTPS API, not SMTP)
            mailerSend.emails().send(email);
            log.info("Email sent successfully to {}", toEmail);
            System.out.println("Email sent successfully!"); // Keep your original print for console visibility
            return true;

        } catch (MailerSendException e) {
            log.error("MailerSend API error for {}: {}", toEmail, e.getMessage(), e); // Log full exception
            System.err.println("MailerSend Error: " + e.getMessage()); // For immediate visibility
            return false;
        } catch (IOException e) {
            log.error("IO error (e.g., file attachment) for {}: {}", toEmail, e.getMessage(), e);
            System.err.println("IO Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error for {}: {}", toEmail, e.getMessage(), e);
            System.err.println("Unexpected Error: " + e.getMessage());
            return false;
        }
    }
}