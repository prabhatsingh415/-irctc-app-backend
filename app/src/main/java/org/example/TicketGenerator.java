package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class TicketGenerator {

    // Method to generate and print a ticket for a passenger
    public String printTicket(String ticketId, int trainId, String trainType, Date arrivalTime,Date departureTime, String passangerName, String arrival, String destination,Date travelDate) {
        String path = "app/src/main/resources/Ticket.jpg";  // File path for the generated ticket
        try {
            // Load the background image for the ticket

            InputStream backgroundImage = getClass().getClassLoader().getResourceAsStream("resources/Ticket-BackgroundImage.jpg");
//            System.out.println("File exists: " + backgroundImage.exists());
//            System.out.println("Absolute Path: " + backgroundImage.getAbsolutePath());
            assert backgroundImage != null;
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
            g2d.setFont(new Font("Arial", Font.PLAIN, 25));
            g2d.setColor(Color.BLACK);

            // Padding and height for text positioning
            int padding = 25;
            int heightLine = 40;

            // Draw ticket details on the image
            g2d.drawString("Ticket ID          :- " + ticketId, padding, padding + heightLine * 2);
            g2d.drawString("Train No           :- " + trainId, padding, padding + heightLine * 3);
            g2d.drawString("Train Type         :- " + trainType, padding, padding + heightLine * 4);
            g2d.drawString("Passenger Name     :- " + passangerName, padding, padding + heightLine * 5);
            g2d.drawString("Arrival Station    :- " + arrival, padding, padding + heightLine * 6);
            g2d.drawString("Departure Station  :- " + destination, padding, padding + heightLine * 7);
            g2d.drawString("Arrival Time       :- " + arrivalTime, padding, padding + heightLine * 8);
            g2d.drawString("Departure Time     :- " + departureTime, padding, padding + heightLine * 9);
            g2d.drawString("Travel Time        :- " + travelDate, padding, padding + heightLine * 10);
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
            e.printStackTrace();
            throw new RuntimeException(e);  // Rethrow the exception for further handling if needed
        }
        return path;  // Return the file path of the printed ticket
    }
}
