package org.example.services;

import java.util.Scanner;

public class BookingServices {
    Scanner scanner = new Scanner(System.in);
    TrainServices trainServices = new TrainServices();
    TicketServices ticketServices = new TicketServices();
    public void bookingStatus(){
    }
    public void cancelBooking(){
        System.out.println("Enter Your Ticket Id ");
        String ticketId = scanner.nextLine();
        if(ticketServices.cancelTicket(ticketId)){
            System.out.println("Your Ticket Is Canceled");
        }else{
            System.out.println("We could not find a ticket with that ID. Could you please check and try again?");
        }
    }
    public void bookTicket(String email){
        System.out.println("Enter The origin platform ");
        String originLocation = scanner.nextLine();
        System.out.println("Enter The Destination");
        String destinationLocation = scanner.nextLine();
        System.out.println("Select By Which Train You Want To go");
        trainServices.searchTrain(originLocation,destinationLocation);
        System.out.println("Enter Train Id");
        int trainId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter the Date Of Travel");
        String dateOfTravel = scanner.nextLine();
        System.out.println("Enter The Passenger Name");
        String name = scanner.nextLine();
        ticketServices.bookTicket(trainId,destinationLocation,originLocation,dateOfTravel,name,email);
    }
}
