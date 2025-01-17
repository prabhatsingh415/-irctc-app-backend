package org.example.dataBase;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DataBaseConfig {
    // Loading environment variables from the .env file for security purposes.
    static Dotenv dotenv = Dotenv.configure()
            .directory("C:\\Users\\acer\\Irctc\\app\\.env")
            .load();

    // Declaring the connection and credentials required to connect to the database.
    private static Connection connection;
    private static final String url = dotenv.get("DATABASE_URL");  // Get URL directly from .env file
    private static final String userName = dotenv.get("DATABASE_USERNAME", "root");
    private static final String password = dotenv.get("DATABASE_PASSWORD");
    public static Connection createConnection() {
        try {
            // Try to establish a connection to the database using the details from .env.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);

        } catch (Exception e) {
            // Provide a user-friendly error message.
            System.out.println("Oops! Something went wrong while connecting to the database. Please try again later.");
            // Optionally, you can log this exception in a file or send a report to admin.
        }
        return connection;  // Return the connection object (or null if failed).
    }

}
