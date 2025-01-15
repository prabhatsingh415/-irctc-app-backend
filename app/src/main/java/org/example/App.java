package org.example;

import org.example.dataBase.UserDao;
import org.example.entities.User;
import org.example.pages.HomePage;
import org.example.services.VerificationEmail;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        UserDao userDao = new UserDao();// UserDao Class Instance for login and signUp
        HomePage home = new HomePage();
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("Welcome To IRCTC - Ticket Booking APP");
            System.out.println("Enter 1 For SIGN-UP ");
            System.out.println("Enter 2 For Login");
            System.out.println("Enter 3 For Sign-UP(for employees)");
            System.out.println("Enter 4 For Exit");
            choice = scanner.nextInt();
            scanner.nextLine();

            String name;
            String email;

            switch (choice) {
                case 1:

                    while (true) {
                        System.out.println("Welcome To IRCTC - Ticket Booking APP");
                        scanner.nextLine();
                        System.out.println("Enter Your Name:");
                        name = scanner.nextLine();
                        System.out.println("Enter Your Email For Verification:");
                        email = scanner.nextLine();
                        VerificationEmail mail = new VerificationEmail();

                        if (mail.sendEmail(email, name)) {
                            System.out.println("We've sent a verification email. Please check your inbox and enter the verification code below.");
                            // Wait for user to enter the code
                            System.out.println("Enter Verification Code:");
                            int userInputCode = scanner.nextInt();
                            scanner.nextLine();


                            if (mail.authenticator(userInputCode)) {
                                System.out.println("Email verified successfully!");
                                System.out.println("Set New Password");
                                String hashedPassword = Utilities.passwordEncryptor(scanner.next()) ;
                                User user = new User(name,email,hashedPassword);
                                userDao.registerUser(user);
                                home.displayHomePage(email);
                                break;
                            } else {

                                System.out.println("Entered Wrong Code.");
                                System.out.println("Would you like to:");
                                System.out.println("1. Resend Code");
                                System.out.println("2. Exit");
                                choice = scanner.nextInt();
                                scanner.nextLine();

                                if (choice == 1) {
                                    continue;
                                } else {
                                    System.out.println("Exiting the program.");
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Something went wrong while sending the email. Please try again.");
                            System.out.println("Would you like to:");
                            System.out.println("1. Re-enter your information");
                            System.out.println("2. Exit");
                            choice = scanner.nextInt();
                            scanner.nextLine();

                            if (choice == 2) {
                                System.out.println("Exiting the program.");
                                break;
                            }

                        }
                    }

                    break;

                case 2:
                    System.out.println("Enter your Email");
                    scanner.nextLine();
                    email = scanner.nextLine();
                    System.out.println("Enter Your PassWord");
                    String password = scanner.nextLine();

                    if(userDao.login(email, password)){
                       home.displayHomePage(email);
                    }else{
                        System.out.println("Wrong passWord or Email");
                    }
                     break;
                case 3:
                    System.out.println("Enter you Employee ID");
                    int id = scanner.nextInt();
                    System.out.println("Enter your name");
                    name = scanner.next();
                    System.out.println("Enter Your Email");
                    email = scanner.next();
                    System.out.println("Enter your Password");
                   //   password = scanner.next();

                    break;

                case 4:
                    System.out.println("Exiting the application. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        }
    }
}
