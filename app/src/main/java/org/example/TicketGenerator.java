package org.example;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TicketGenerator {

    public String printTicket(int trainId,String passangerName,String arrival,String destination){
        String path  = "app/src/main/resources/Ticket.jpg";
        try {
            File backgroundImage = new File("app/src/main/resources/Ticket-BackgroundImage.jpg");
            BufferedImage image = ImageIO.read(backgroundImage);
            Graphics2D g2d  = image.createGraphics();

            g2d.setFont(new Font("Arial",Font.BOLD,40));
            g2d.setColor(Color.blue);

            String title = "IRCTC TICKET BOOKING APP";
            int titleX = 500;
            int titleY = 80;

            g2d.drawString(title,titleX,titleY);

            g2d.setFont(new Font("Arial",Font.PLAIN,40));
            g2d.setColor(Color.BLACK);

            int padding = 65;
            int heightLine = 80;

            g2d.drawString("Train No  :- "+trainId,padding,padding + heightLine);
            g2d.drawString("Passenger Name  :- "+passangerName,padding,padding + heightLine * 2);
            g2d.drawString("Arrival  :- "+arrival,padding,padding + heightLine * 3);
            g2d.drawString("Departure  :- "+destination,padding,padding + heightLine * 4);

            g2d.setFont(new Font("Arial",Font.BOLD,15));
            g2d.setColor(Color.BLACK);

            String footer = "DEVELOPED BY PRABHAT SINGH | CONTACT  Email:- singh.prabhat.work@gmail.com ";
            int footerX = 500;
            int footerY = image.getHeight() - 30;

            g2d.drawString(footer,footerX,footerY);


            g2d.dispose();
            File printedTicket = new File(path);
            ImageIO.write(image,"jpg",printedTicket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }
}
