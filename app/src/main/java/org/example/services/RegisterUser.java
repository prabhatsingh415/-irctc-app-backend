package org.example.services;

import org.example.Utilities;
import org.example.dataBase.UserDao;
import org.example.entities.User;
import org.example.pages.HomePage;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RegisterUser {
    Scanner scanner = new Scanner(System.in);
    UserDao userDao = new UserDao(); // UserDao Class Instance for login and signUp
    HomePage home = new HomePage();

    String name = " ";
    String email = " ";

    public void signUPUser() {
        boolean isRunning = true;

        while (isRunning) {
            try {
                System.out.println("Enter Your Name:");
                name = scanner.nextLine();

                System.out.println("Enter Your Email For Verification:");
                email = scanner.nextLine();
                VerificationEmail mail = new VerificationEmail();
                if (mail.isEmailAvailable(email)) {
                    System.out.println("Email is available for registration.");
                } else {
                    System.out.println("This email is already registered. Please enter another email.");
                    continue; // Restart the loop and ask for a new email
                }
                if (mail.sendEmail(email, name)) {
                    System.out.println("Verification email sent. Please check your inbox.");

                    System.out.println("Enter Verification Code:");
                    int userInputCode = scanner.nextInt();
                    scanner.nextLine(); // Clear the scanner buffer

                    if (mail.authenticator(userInputCode)) {
                        System.out.println("Email verified successfully!");

                        System.out.println("Set New Password:");
                        String hashedPassword = Utilities.passwordEncryptor(scanner.next());
                        scanner.nextLine(); // Clear the scanner buffer

                        User user = new User(name, email, hashedPassword);
                        userDao.registerUser(user);

                        home.displayHomePage(email);
                        isRunning = false; // Exit the loop
                    } else {
                        handleResendOrExit();
                    }
                } else {
                    System.out.println("Failed to send verification email.");
                    handleResendOrExit();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void handleResendOrExit() {
        System.out.println("Would you like to:");
        System.out.println("1. Resend Code");
        System.out.println("2. Exit");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer
            if (choice != 1) {
                System.out.println("Exiting the program.");
                System.exit(0);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Exiting the program.");
            System.exit(0);
        }
    }

    public void loginUser() {
        System.out.println("Enter your Email:");
        String email = scanner.nextLine(); // Reads the email input

        System.out.println("Enter Your Password:");
        String password = scanner.nextLine(); // Reads the password input

        if (userDao.login(email, password)) {
            home.displayHomePage(email); // Navigates to the home page if login succeeds
        } else {
            System.out.println("Wrong password or Email");
        }
    }

}
