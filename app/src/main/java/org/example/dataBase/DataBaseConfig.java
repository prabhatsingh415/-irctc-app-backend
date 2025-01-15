package org.example.dataBase;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;


public class DataBaseConfig {
    static Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\acer\\Irctc\\app\\.env").load();
  static Connection connection;
  private static final String url = "jdbc:mysql://localhost:3306/irctc_db?useSSL=false&allowPublicKeyRetrieval=true";

    private static  final String userName = "root";
    private  static  final  String password =  dotenv.get("DATABASE_PASSWORD");
    public static Connection createConnection(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url,userName,password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
