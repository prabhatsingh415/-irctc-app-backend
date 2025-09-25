package org.example.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class EmailSender {

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);
    private final String fromEmail;
    private final String brevoApiKey;

    private static final String API_URL = "https://api.brevo.com/v3/smtp/email";

    public EmailSender(String fromEmail) {
        this.fromEmail = fromEmail;
        this.brevoApiKey = System.getenv("BREVO_API_KEY");

        if (this.brevoApiKey == null || this.brevoApiKey.trim().isEmpty()) {
            log.error("BREVO_API_KEY is missing or empty");
            throw new RuntimeException("BREVO_API_KEY not found! Please check your environment variables.");
        }
        if (this.fromEmail == null || this.fromEmail.trim().isEmpty()) {
            log.error("From email is missing or empty");
            throw new RuntimeException("EMAIL_USERNAME not found! Please check your environment variables.");
        }
    }

    public boolean sendEmail(String toName, String toEmail, String subject, String message, String filePath) {
        log.info("Attempting to send email to: {}", toEmail);

        OkHttpClient client = new OkHttpClient();

        JsonObject json = new JsonObject();

        JsonObject sender = new JsonObject();
        sender.addProperty("name", "IRCTC");
        sender.addProperty("email", fromEmail);
        json.add("sender", sender);

        JsonObject toRecipient = new JsonObject();
        toRecipient.addProperty("name", toName);
        toRecipient.addProperty("email", toEmail);
        JsonArray toArray = new JsonArray();
        toArray.add(toRecipient);
        json.add("to", toArray);

        json.addProperty("subject", subject);
        json.addProperty("textContent", message);


        if (filePath != null && !filePath.trim().isEmpty()) {
            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                String base64File = Base64.getEncoder().encodeToString(fileBytes);

                JsonObject attachment = new JsonObject();
                attachment.addProperty("content", base64File);
                attachment.addProperty("name", Paths.get(filePath).getFileName().toString());

                JsonArray attachments = new JsonArray();
                attachments.add(attachment);

                json.add("attachments", attachments);

                log.info("Attachment added: {}", filePath);
            } catch (IOException e) {
                log.error("Failed to read attachment file: {}", e.getMessage(), e);
            }
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("accept", "application/json")
                .addHeader("api-key", brevoApiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = (response.body() != null) ? response.body().string() : "No response body";

            if (response.isSuccessful()) {
                log.info("Brevo API success: {} - {}", response.code(), responseBody);
                return true;
            } else {
                log.error("Brevo API error: {} {} - {}", response.code(), response.message(), responseBody);
                return false;
            }
        } catch (IOException e) {
            log.error("IO error while sending email: {}", e.getMessage(), e);
            return false;
        }
    }
}
