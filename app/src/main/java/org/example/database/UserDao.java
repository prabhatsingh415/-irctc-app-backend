package org.example.database;

import org.example.utilities.Utilities;
import org.example.entities.User;
import java.sql.*;

import static org.example.database.DataBaseConfig.createConnection;

public class UserDao {
    // Method to register a new user
    public void registerUser(User user) {
        String userName = user.getName();
        String userEmail = user.getMail();
        String userPassword = user.getHashedPassWord();


        String checkUserQuery = "SELECT COUNT(*) FROM user WHERE UserName = ? AND UserEMAIL = ?";
        String insertQuery = "INSERT INTO user (UserName, UserEMAIL, UserPassword) VALUES (?, ?, ?)";

        try (Connection connection = DataBaseConfig.createConnection();
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
                    insertStatement.setString(3, userPassword);
                    insertStatement.executeUpdate();

                    System.out.println("Sign Up Successfully!");
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
        String hashedPassword = null;
        String query = "SELECT UserPassword FROM user WHERE UserEMAIL = ?";

        try (Connection connection = createConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Invalid email or password");
                    return false;
                }
                hashedPassword = rs.getString("UserPassword");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while logging in. Please try again later.");
            return false;
        }

        if (hashedPassword == null) return false;
        return Utilities.checkPassword(password, hashedPassword);
    }
}
