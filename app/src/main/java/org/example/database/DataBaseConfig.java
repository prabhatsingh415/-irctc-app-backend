package org.example.database;
import java.sql.Connection;
import java.sql.DriverManager;


public class DataBaseConfig {
    // Declaring the connection and credentials required to connect to the database.
    private static Connection connection;

    private static final String url = System.getenv("DATABASE_URL");
    private static final String userName = System.getenv("DATABASE_USERNAME");
    private static final String password = System.getenv("DATABASE_PASSWORD");

    public static Connection createConnection() {
        try {
            // Try to establish a connection to the database using the details from .env
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);

        } catch (Exception e) {

            System.out.println("Oops! Something went wrong while connecting to the database. Please try again later.");
        }
        return connection;  // Return the connection object (or null if failed).
    }

}
