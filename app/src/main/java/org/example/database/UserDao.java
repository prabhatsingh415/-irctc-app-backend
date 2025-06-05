package org.example.database;

import org.example.Utilities;
import org.example.entities.User;

import java.sql.*;

import static org.example.database.DataBaseConfig.createConnection;

public class UserDao {
    Utilities utilities = new Utilities();
    // Method to register a new user
    public void registerUser(User user) {
        String userName = user.getName();
        String userEmail = user.getMail();
        String userPassword = user.getHashedPassWord();


        String checkUserQuery = "SELECT COUNT(*) FROM user WHERE UserName = ? AND UserEMAIL = ?";
        String insertQuery = "INSERT INTO user (UserName, UserEMAIL, UserPassword) VALUES (?, ?, ?)";

        try (Connection connection = createConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkUserQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            // Set parameters for check query
            checkStatement.setString(1, userName);
            checkStatement.setString(2, userEmail);

            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                if (count == 0) {
                    // Insert new user
                    insertStatement.setString(1, userName);
                    insertStatement.setString(2, userEmail);
                    insertStatement.setString(3, userPassword); // Already hashed before passing to this method
                    insertStatement.executeUpdate();

                    System.out.println("Sign Up Successfully!");
                    new HomePage().displayHomePage(userEmail);
                } else {
                    System.out.println("User already exists.");
                }
            }

        } catch (SQLException e) {
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
