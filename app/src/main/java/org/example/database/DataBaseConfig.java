package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;


public class DataBaseConfig {
    // Declaring the connection and credentials required to connect to the database.
    private static Connection connection;

    private static final String url = System.getenv("DATABASE_URL");  // Get URL directly from .env file
    private static final String userName = System.getenv("DATABASE_USERNAME");
    private static final String password = System.getenv("DATABASE_PASSWORD");

    static {
        if(url==null || userName==null || password==null){
            throw new RuntimeException("Database environment variables are not set.");
        }
    }
    public static Connection createConnection() {
        try {
            // Try to establish a connection to the database using the details from .env
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
