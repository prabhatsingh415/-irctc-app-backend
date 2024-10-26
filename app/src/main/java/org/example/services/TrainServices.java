package org.example.services;

import org.example.dataBase.DataBaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TrainServices {
    public static void main(String[] args) {
        findRoute(1234);
    }
    private static void findTrain(String departure, String arrival){

        try {
            Connection connection;
            connection = DataBaseConfig.createConnection();
            String query = "SELECT td.TrainName, td.TrainID, td.TrainType, td.TotalSeats, "
                    + "td.SourceStations, td.DestinationStation, td.DepartureTime, td.ArrivalTime "
                    + "FROM TrainDetails td "
                    + "JOIN TrainStation ts1 ON td.TrainID = ts1.TrainID "
                    + "JOIN TrainStation ts2 ON td.TrainID = ts2.TrainID "
                    + "JOIN StationDetails sd1 ON ts1.StationID = sd1.StationID "
                    + "JOIN StationDetails sd2 ON ts2.StationID = sd2.StationID "
                    + "WHERE sd1.StationName = ? "
                    + "AND sd2.StationName = ? "
                    + "AND ts1.StationID != ts2.StationID";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,departure);
            statement.setString(2,arrival);

            // Execute query

          java.sql.ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String trainName = rs.getString("TrainName");
                int trainID = rs.getInt("TrainID");
                String trainType = rs.getString("TrainType");
                int totalSeats = rs.getInt("TotalSeats");
                String sourceStations = rs.getString("SourceStations");
                String destinationStation = rs.getString("DestinationStation");
                String departureTime = rs.getString("DepartureTime");
                String arrivalTime = rs.getString("ArrivalTime");

                // Print or process the results
                System.out.printf("Train Name: %s\n Train ID: %d\n Train Type: %s\n Total Seats: %d\n Source: %s\n Destination: %s\n Departure: %s\n Arrival: %s%n",
                        trainName, trainID, trainType, totalSeats, sourceStations, destinationStation, departureTime, arrivalTime);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void findRoute(int trainID){
        try {
            Connection connection =  DataBaseConfig.createConnection();
            String query = "SELECT sd.StationName "
                    + "FROM TrainStation ts "
                    + "JOIN StationDetails sd ON ts.StationID = sd.StationID "
                    + "WHERE ts.TrainID = ? "
                    + "ORDER BY sd.StationID";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1,trainID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String stationName = rs.getString("StationName");
                // Print or process the station details
                System.out.printf("Station: %s\n ",stationName);
            }

        }catch (Exception e ){
            e.printStackTrace();
        }
    }
}
