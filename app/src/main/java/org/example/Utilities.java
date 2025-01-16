package org.example;

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
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
        return BCrypt.checkpw(userInput, hashedPassword);
    }
}
