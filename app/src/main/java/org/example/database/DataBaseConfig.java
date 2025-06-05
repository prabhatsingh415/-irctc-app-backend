package org.example.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseConfig {
    private static HikariDataSource dataSource;

    // Initialize once during class loading
    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(System.getenv("DATABASE_URL"));
            config.setUsername(System.getenv("DATABASE_USERNAME"));
            config.setPassword(System.getenv("DATABASE_PASSWORD"));
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            config.setMaximumPoolSize(5);      //Maximum concurrent connections
            config.setMinimumIdle(2);  // free connections available initially
            config.setIdleTimeout(30000);        // 30 seconds
            config.setConnectionTimeout(30000);  // 30 seconds

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize HikariCP: " + e.getMessage());
        }
    }

    // Public method to get connection
    public static Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Optional: expose DataSource if needed
    public static DataSource getDataSource() {
        return dataSource;
    }
}
