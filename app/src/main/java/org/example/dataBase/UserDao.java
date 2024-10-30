package org.example.dataBase;

import org.example.entities.User;
import org.example.pages.HomePage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.dataBase.DataBaseConfig.createConnection;

public class UserDao {
    public void registerUser(User user) {
        String userName = user.getName();
        String userEmail = user.getMail();
        String userPassword = user.getHashedPassWord();

        // Define queries
        String insertQuery = "INSERT INTO User (UserName, UserEmail, UserPassword) VALUES (?, ?, ?)";
        String checkUserQuery = "SELECT COUNT(*) FROM User WHERE UserName = ? AND UserEmail = ? AND UserPassword = ?";

        try (Connection connection = createConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkUserQuery);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set parameters for check user query
            checkStatement.setString(1, userName);
            checkStatement.setString(2, userEmail);
            checkStatement.setString(3, userPassword);

            // Execute check user query
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                HomePage homePage = new HomePage();
                if (count == 0) {
                    // User does not exist, insert new user
                    preparedStatement.setString(1, userName);
                    preparedStatement.setString(2, userEmail);
                    preparedStatement.setString(3, userPassword);
                    preparedStatement.executeUpdate();
                    System.out.println("Sign Up Successfully!");
                    homePage.displayHomePage(userName);
                } else {
                    // User exists
                    System.out.println("Log In Successfully!");
                    homePage.displayHomePage(userName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
