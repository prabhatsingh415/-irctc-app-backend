package org.example.dataBase;

import org.example.Utilities;
import org.example.entities.User;
import org.example.pages.HomePage;

import java.sql.*;

import static org.example.dataBase.DataBaseConfig.createConnection;

public class UserDao {

    // Method to register a new user
    public void registerUser(User user) {
        String userName = user.getName();
        String userEmail = user.getMail();
        String userPassword = user.getHashedPassWord();

        // Define SQL queries to check if the user already exists and to insert a new user
        String insertQuery = "INSERT INTO User (UserName, UserEmail, UserPassword) VALUES (?, ?, ?)";
        String checkUserQuery = "SELECT COUNT(*) FROM User WHERE UserName = ? AND UserEmail = ? AND UserPassword = ?";

        // Try-with-resources ensures that the resources (Connection, PreparedStatement, ResultSet) are closed automatically
        try (Connection connection = createConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkUserQuery);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set parameters for the query that checks if the user exists
            checkStatement.setString(1, userName);
            checkStatement.setString(2, userEmail);
            checkStatement.setString(3, userPassword);

            // Execute the query to check if the user already exists
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                HomePage homePage = new HomePage();
                if (count == 0) {
                    // If the user does not exist, insert a new user into the database
                    preparedStatement.setString(1, userName);
                    preparedStatement.setString(2, userEmail);
                    preparedStatement.setString(3, userPassword);
                    preparedStatement.executeUpdate();
                    System.out.println("Sign Up Successfully!"); // Success message
                    homePage.displayHomePage(userEmail); // Redirect to home page
                } else {
                    System.out.println("User already exists."); // If user already exists, show a message
                }
            }
        } catch (SQLException e) {
            // Catch any SQL exceptions and show a user-friendly message
            System.out.println("Error occurred while registering the user. Please try again later.");
        }
    }

    // Method to log in a user
    public boolean login(String email, String password) {
        String hashedPassword = null; // To store the fetched hashed password from the database

        // SQL query to get the stored password for the given email
        String query = "SELECT UserPassword FROM user WHERE UserEMAIL = ?";

        // Try-with-resources ensures connection is closed automatically after usage
        try (Connection connection = createConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                // If no matching email is found, show a user-friendly message
                System.out.println("Invalid email or password");
            } else {
                // If the email is found, retrieve the hashed password from the database
                hashedPassword = rs.getString("UserPassword");
            }
        } catch (SQLException e) {
            // Catch SQL exceptions and show a user-friendly message
            System.out.println("An error occurred while logging in. Please try again later.");
        }

        // Return the result of the password check (whether the provided password matches the stored hashed password)
        return Utilities.checkPassword(password, hashedPassword);
    }
}
