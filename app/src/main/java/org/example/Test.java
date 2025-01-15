package org.example;

import org.example.services.TicketServices;

public class Test {
    public static void main(String[] args) {
        TicketServices ticketServices = new TicketServices();
               ticketServices.bookTicket(1234,"kota","jaipur","16-01-2025","psr","vandnaaingh2001@gmail.com");
    }
}
