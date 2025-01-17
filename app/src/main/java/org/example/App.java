package org.example;


import org.example.services.RegisterUser;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        RegisterUser registerUser = new RegisterUser();
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("Welcome To IRCTC - Ticket Booking APP");
            System.out.println("Enter 1 For SIGN-UP ");
            System.out.println("Enter 2 For Login");
            System.out.println("Enter 3 For Exit");
            choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Welcome To IRCTC - Ticket Booking APP");
            switch (choice) {
                case 1:
                     registerUser.signUPUser();
                     break;
                case 2:
                    registerUser.loginUser();
                    break;
                case 3:
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
