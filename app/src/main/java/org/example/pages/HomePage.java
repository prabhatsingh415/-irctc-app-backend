package org.example.pages;

import org.example.services.BookingServices;
import org.example.services.TicketServices;
import org.example.services.TrainServices;

import java.util.Scanner;

public class HomePage {
    public void displayHomePage(String name) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome " + name);
        int choice;
        while (true) {
            System.out.println("Welcome To IRCTC - Ticket Booking APP");
            System.out.println("Enter 1 For Search Train");
            System.out.println("Enter 2 For Book Ticket");
            System.out.println("Enter 3 For Cancel Ticket");
            System.out.println("Enter 4 For download Ticket");
            System.out.println("Enter 5 To Check Booking Status");
            System.out.println("Enter 6 To Exit");
            choice = scanner.nextInt();
            TrainServices train = new TrainServices();
            BookingServices bookingServices = new BookingServices();
            TicketServices ticketServices = new TicketServices();
            switch (choice) {
                case 1:
                    train.searchTrain();
                    break;
                case 2:
                    bookingServices.bookTicket();
                    break;
                case 3:
                    bookingServices.cancelBooking();
                    break;
                case 4:
                    bookingServices.bookingStatus();
                    break;
                case 5:
                    ticketServices.getTicket();
                    break;
                case 6:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
