package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TicketGenerator {

    // Method to generate and print a ticket for a passenger
    public String printTicket(int trainId, String passangerName, String arrival, String destination) {
        String path = "app/src/main/resources/Ticket.jpg";  // File path for the generated ticket
        try {
            // Load the background image for the ticket
            File backgroundImage = new File("app/src/main/resources/Ticket-BackgroundImage.jpg");
            BufferedImage image = ImageIO.read(backgroundImage);
            Graphics2D g2d = image.createGraphics();

            // Set font and color for the title text
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.setColor(Color.blue);

            // Title of the ticket
            String title = "IRCTC TICKET BOOKING APP";
            int titleX = 500;
            int titleY = 80;

            // Draw the title on the ticket
            g2d.drawString(title, titleX, titleY);

            // Set font and color for the rest of the text
            g2d.setFont(new Font("Arial", Font.PLAIN, 40));
            g2d.setColor(Color.BLACK);

            // Padding and height for text positioning
            int padding = 65;
            int heightLine = 80;

            // Draw ticket details on the image
            g2d.drawString("Train No  :- " + trainId, padding, padding + heightLine);
            g2d.drawString("Passenger Name  :- " + passangerName, padding, padding + heightLine * 2);
            g2d.drawString("Arrival  :- " + arrival, padding, padding + heightLine * 3);
            g2d.drawString("Departure  :- " + destination, padding, padding + heightLine * 4);

            // Set font and color for the footer text
            g2d.setFont(new Font("Arial", Font.BOLD, 15));
            g2d.setColor(Color.BLACK);

            // Footer message with contact information
            String footer = "DEVELOPED BY PRABHAT SINGH | CONTACT  Email:- singh.prabhat.work@gmail.com ";
            int footerX = 500;
            int footerY = image.getHeight() - 30;

            // Draw the footer on the ticket
            g2d.drawString(footer, footerX, footerY);

            // Dispose of the graphics object after drawing
            g2d.dispose();

            // Save the generated ticket as a JPEG file
            File printedTicket = new File(path);
            ImageIO.write(image, "jpg", printedTicket);

        } catch (IOException e) {
            // If an error occurs, display a user-friendly message
            System.out.println("An error occurred while generating the ticket. Please try again.");
            throw new RuntimeException(e);  // Rethrow the exception for further handling if needed
        }
        return path;  // Return the file path of the printed ticket
    }
}
