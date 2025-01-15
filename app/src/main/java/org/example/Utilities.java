package org.example;

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Utilities {

    public static void deleteTicket(){
        String filePath = "app/src/main/resources/Ticket.jpg";
        // Schedule the deletion of this specific file after 2 days
        long delay = TimeUnit.DAYS.toMillis(2); // 2 days in milliseconds

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable deleteTask = () -> {
            File file = new File(filePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("Ticket.jpg file deleted successfully: " + filePath);
                } else {
                    System.out.println("Failed to delete Ticket.jpg: " + filePath);
                }
            } else {
                System.out.println("Ticket.jpg does not exist: " + filePath);
            }
        };

// Schedule the delete task to run after 2 days
        scheduler.schedule(deleteTask, delay, TimeUnit.MILLISECONDS);

// Optionally, shutdown the scheduler after all tasks are completed
        scheduler.shutdown();

    }
    public static String passwordEncryptor(String password){
     return BCrypt.hashpw(password,BCrypt.gensalt());
    }

    public static boolean checkPassword(String userInput , String hashedPassWord){
        return BCrypt.checkpw(userInput,hashedPassWord);
    }
}
