package org.example.utilities;

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Utilities {

    // Method to delete the ticket after a delay of 2 days
    public static void deleteTicket() {
        String filePath = "app/src/main/resources/Ticket.jpg";

        // Schedule the deletion of this specific file after 2 days
        long delay = TimeUnit.DAYS.toMillis(2); // Convert 2 days to milliseconds

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable deleteTask = () -> {
            File file = new File(filePath);
            if (file.exists()) {
                // Attempt to delete the file
                boolean deleted = file.delete();
                if (deleted) {
                    // Success message when file is deleted
                    System.out.println("Ticket.jpg file deleted successfully: " + filePath);
                } else {
                    // Error message when file deletion fails
                    System.out.println("Failed to delete Ticket.jpg: " + filePath);
                }
            } else {
                // Message if the file doesn't exist
                System.out.println("Ticket.jpg does not exist: " + filePath);
            }
        };

        // Schedule the delete task to run after 2 days
        scheduler.schedule(deleteTask, delay, TimeUnit.MILLISECONDS);

        // Shutdown the scheduler after task completion
        scheduler.shutdown();
    }

    // Method to encrypt the password using BCrypt hashing
    public static String passwordEncryptor(String password) {
        // Hash and salt the password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Method to check if the entered password matches the hashed password
    public static boolean checkPassword(String userInput, String hashedPassword) {
        // Check if the user input matches the hashed password
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(userInput, hashedPassword);
    }

    public static boolean isDateValid(String date) {
        try {
            // Parse the entered date and compare it with the current date
            LocalDate travelDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate today = LocalDate.now();

            // Check if the travel date is today or later
            return !travelDate.isBefore(today);
        } catch (DateTimeParseException e) {
            // If the date is not parsable, return false
            return false;
        }
    }

    public static String generateUniqueId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // generating uique
    }

    // Generate a random 4-digit verification code
    private int code() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // 1000-9999
    }
    int code = code();

    // Check if user input matches generated code
    public boolean authenticator(int userInput) {
        return userInput == code;
    }
}
