package org.example.pages;

import org.example.services.BookingServices;
import org.example.services.TrainServices;

import java.util.Scanner;

public class HomePage {
    public void displayHomePage(String userEmail) {
        Scanner scanner = new Scanner(System.in);

        int choice;
        while (true) {
            System.out.println("Welcome To IRCTC - Ticket Booking APP");
            System.out.println("Enter 1 For Search Train");
            System.out.println("Enter 2 For Book Ticket");
            System.out.println("Enter 3 For Cancel Ticket");
            System.out.println("Enter 4 To Exit");
            choice = scanner.nextInt();
            scanner.nextLine();
            TrainServices train = new TrainServices();
            BookingServices bookingServices = new BookingServices();
            switch (choice) {
                case 1:
                    System.out.println("Enter The Arrival Station");
                    String arrival = scanner.nextLine();

                    System.out.println("Enter The Destination Station");
                    String destination = scanner.nextLine();
                    train.searchTrain(arrival,destination);
                    break;
                case 2:
                    bookingServices.bookTicket(userEmail);
                    break;
                case 3:
                    bookingServices.cancelBooking();
                    break;
                case 4:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
