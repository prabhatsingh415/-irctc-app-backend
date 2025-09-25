package org.example.services;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class EmailSender {

    private final String fromEmail;
    private final String sendGridApiKey;

    public EmailSender(String fromEmail) {
        this.fromEmail = fromEmail;
        this.sendGridApiKey = System.getenv("SENDGRID_API_KEY");

        if (this.sendGridApiKey == null) {
            throw new RuntimeException("SENDGRID_API_KEY not found! Please check Render env variables.");
        }
    }

    public boolean sendEmail(String to, String subject, String message, String filePath) {
        Email from = new Email(fromEmail);
        Email recipient = new Email(to);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, recipient, content);

        if (filePath != null && !filePath.isEmpty()) {
            try {
                Path path = Paths.get(filePath);
                byte[] fileData = Files.readAllBytes(path);
                Attachments attachment = new Attachments();
                attachment.setContent(Base64.getEncoder().encodeToString(fileData));
                attachment.setType("application/octet-stream");
                attachment.setFilename(path.getFileName().toString());
                attachment.setDisposition("attachment");
                mail.addAttachments(attachment);
            } catch (IOException e) {
                System.err.println("Failed to attach file: " + e.getMessage());
            }
        }

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("SendGrid Status Code: " + response.getStatusCode());
            return response.getStatusCode() == 202;
        } catch (IOException ex) {
            System.err.println("SendGrid Error: " + ex.getMessage());
            return false;
        }
    }
}
