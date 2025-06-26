package org.example.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class TicketGenerator {

    // Method to generate and print a ticket for a passenger
    public String printTicket(String ticketId, int trainId, String trainType, Date arrivalTime, Date departureTime, String passangerName, String arrival, String destination, Date travelDate) {
        String path = "Ticket_" + ticketId + ".jpg";  // Path where final image will be saved

        try {
            // ✅ FIXED PATH
            InputStream backgroundImage = getClass().getClassLoader().getResourceAsStream("Ticket-BackgroundImage.jpg");

            if (backgroundImage == null) {
                throw new IllegalArgumentException("❌ Ticket background image not found in classpath!");
            }

            BufferedImage image = ImageIO.read(backgroundImage);
            Graphics2D g2d = image.createGraphics();

            // Title
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.setColor(Color.BLUE);
            g2d.drawString("IRCTC TICKET BOOKING APP", 500, 80);

            // Ticket details
            g2d.setFont(new Font("Arial", Font.PLAIN, 25));
            g2d.setColor(Color.BLACK);
            int padding = 25;
            int heightLine = 40;

            g2d.drawString("Ticket ID          :- " + ticketId, padding, padding + heightLine * 2);
            g2d.drawString("Train No           :- " + trainId, padding, padding + heightLine * 3);
            g2d.drawString("Train Type         :- " + trainType, padding, padding + heightLine * 4);
            g2d.drawString("Passenger Name     :- " + passangerName, padding, padding + heightLine * 5);
            g2d.drawString("Arrival Station    :- " + arrival, padding, padding + heightLine * 6);
            g2d.drawString("Departure Station  :- " + destination, padding, padding + heightLine * 7);
            g2d.drawString("Arrival Time       :- " + arrivalTime, padding, padding + heightLine * 8);
            g2d.drawString("Departure Time     :- " + departureTime, padding, padding + heightLine * 9);
            g2d.drawString("Travel Time        :- " + travelDate, padding, padding + heightLine * 10);

            // Footer
            g2d.setFont(new Font("Arial", Font.BOLD, 15));
            g2d.drawString("DEVELOPED BY PRABHAT SINGH | CONTACT Email:- singh.prabhat.work@gmail.com", 500, image.getHeight() - 30);

            g2d.dispose();

            // ✅ Save to same path where app is running
            File printedTicket = new File(path);
            ImageIO.write(image, "jpg", printedTicket);
            return printedTicket.getAbsolutePath();

        } catch (IOException e) {
            System.out.println("❌ An error occurred while generating the ticket: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
