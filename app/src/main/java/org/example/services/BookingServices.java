package org.example.services;

import java.util.Scanner;

public class BookingServices {

    // Initialize Scanner for user input, and service objects for train and ticket booking
    Scanner scanner = new Scanner(System.in);
    TrainServices trainServices = new TrainServices();
    TicketServices ticketServices = new TicketServices();
    // Method to cancel a booking based on a ticket ID
    public void cancelBooking(){
        System.out.println("Enter Your Ticket Id ");

        // Read ticket ID from user input
        String ticketId = scanner.nextLine();

        // Call cancelTicket method from TicketServices and provide appropriate feedback to the user
        if(ticketServices.cancelTicket(ticketId)){
            System.out.println("Your Ticket Is Canceled");
        }else{
            // In case the ticket ID is not found, inform the user and ask them to check again
            System.out.println("We could not find a ticket with that ID. Could you please check and try again?");
        }
    }

    // Method to book a new ticket by providing details from the user
    public void bookTicket(String email){
        System.out.println("Enter The origin platform ");

        // Read the origin station from the user input
        String originLocation = scanner.nextLine();

        System.out.println("Enter The Destination");

        // Read the destination station from the user input
        String destinationLocation = scanner.nextLine();

        System.out.println("Select By Which Train You Want To go");

        // Call searchTrain from TrainServices to search for trains based on origin and destination
        trainServices.searchTrain(originLocation, destinationLocation);

        System.out.println("Enter Train Id");

        // Read the train ID selected by the user
        int trainId = scanner.nextInt();
        scanner.nextLine(); // Consume the leftover newline character

        System.out.println("Enter the Date Of Travel");

        // Read the travel date from the user input
        String dateOfTravel = scanner.nextLine();

        System.out.println("Enter The Passenger Name");

        // Read the passenger name from the user input
        String name = scanner.nextLine();

        // Call bookTicket method from TicketServices to book the ticket with the given details
        ticketServices.bookTicket(trainId, destinationLocation, originLocation, dateOfTravel, name, email);
    }
}
