package org.example.services;

import java.sql.*;

import static org.example.dataBase.DataBaseConfig.createConnection;

public class TrainServices {

    public void searchTrain(String arrivalStation, String destinationStation) {
        String query = "SELECT DISTINCT t.TrainID, t.TrainName, t.TrainType, t.TotalSeats, " +
                "t.SourceStations, t.DestinationStation, t.DepartureTime, t.ArrivalTime " +
                "FROM TrainDetails t " +
                "JOIN TrainStation tsArrival ON t.TrainID = tsArrival.TrainID " +
                "JOIN StationDetails sArrival ON tsArrival.StationID = sArrival.StationID " +
                "JOIN TrainStation tsDestination ON t.TrainID = tsDestination.TrainID " +
                "JOIN StationDetails sDestination ON tsDestination.StationID = sDestination.StationID " +
                "WHERE LOWER(sArrival.StationName) = LOWER(?) " +  // Match the arrival station
                "AND LOWER(sDestination.StationName) = LOWER(?) " + // Match the destination station
                "AND tsArrival.StationOrder < tsDestination.StationOrder " + // Ensure correct flow of stations
                "AND tsArrival.StationOrder < (SELECT MAX(ts.StationOrder) " +
                "                             FROM TrainStation ts " +
                "                             WHERE ts.TrainID = t.TrainID)"; // Arrival station is not the last station

        try (Connection connection = createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set query parameters
            preparedStatement.setString(1, arrivalStation.trim());
            preparedStatement.setString(2, destinationStation.trim());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No results found.");
            } else {
                do {
                    int trainId = resultSet.getInt("TrainID");
                    String trainName = resultSet.getString("TrainName");
                    String trainType = resultSet.getString("TrainType");
                    int totalSeats = resultSet.getInt("TotalSeats");
                    String sourceStations = resultSet.getString("SourceStations");
                    String destinationStationResult = resultSet.getString("DestinationStation");
                    Time departureTime = resultSet.getTime("DepartureTime");
                    Time arrivalTime = resultSet.getTime("ArrivalTime");

                    // Print train details
                    System.out.println("Train ID: " + trainId);
                    System.out.println("Train Name: " + trainName);
                    System.out.println("Train Type: " + trainType);
                    System.out.println("Total Seats: " + totalSeats);
                    System.out.println("Source Stations: " + sourceStations);
                    System.out.println("Destination Station: " + destinationStationResult);
                    System.out.println("Departure Time: " + departureTime);
                    System.out.println("Arrival Time: " + arrivalTime);

                    // Fetch and print all stations for the train
                    System.out.print("Stations: { ");
                    printStationsForTrain(trainId, connection);
                    System.out.println(" }");
                    System.out.println("------------------------------------------------");
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void printStationsForTrain(int trainId, Connection connection) {
        String query = "SELECT s.StationName " +
                "FROM StationDetails s " +
                "JOIN TrainStation ts ON s.StationID = ts.StationID " +
                "WHERE ts.TrainID = ? " +
                "ORDER BY ts.StationOrder"; // Order by StationOrder

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, trainId);

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean firstStation = true; // To manage comma placement
            while (resultSet.next()) {
                String stationName = resultSet.getString("StationName");
                if (!firstStation) {
                    System.out.print(", ");
                }
                System.out.print(stationName);
                firstStation = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
