package org.example.services;

import com.google.gson.Gson;
import org.example.entities.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import static org.example.database.DataBaseConfig.createConnection;

public class TrainServices {

    public TrainServices() {
        loadStationsFromDB(); // Load station names once when the service starts
    }
    // Method to search for trains based on the arrival and destination stations
    public String searchTrain(String arrivalStation, String destinationStation) {
        List<Train> trainList = new ArrayList<>();
        String jSonData;
        try (Connection connection = createConnection()) {
            // Loop until a train is found or the user decides to exit
                // SQL query to find trains
            String query = "SELECT DISTINCT t.TrainID, t.TrainName, t.TrainType, t.TotalSeats, " +
                    "t.SourceStations, t.DestinationStation, t.DepartureTime, t.ArrivalTime " +
                    "FROM traindetails t " +
                    "JOIN trainstation tsArrival ON t.TrainID = tsArrival.TrainID " +
                    "JOIN stationdetails sArrival ON tsArrival.StationID = sArrival.StationID " +
                    "JOIN trainstation tsDestination ON t.TrainID = tsDestination.TrainID " +
                    "JOIN stationdetails sDestination ON tsDestination.StationID = sDestination.StationID " +
                    "WHERE LOWER(sArrival.StationName) = LOWER(?) " +
                    "AND LOWER(sDestination.StationName) = LOWER(?) " +
                    "AND tsArrival.StationOrder < tsDestination.StationOrder " +
                    "AND tsArrival.StationOrder < (SELECT MAX(ts.StationOrder) " +
                    "                             FROM trainstation ts " +
                    "                             WHERE ts.TrainID = t.TrainID)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    // Set query parameters
                    preparedStatement.setString(1, arrivalStation.trim());
                    preparedStatement.setString(2, destinationStation.trim());

                    // Execute the query
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (!resultSet.next()) {
                        return "No Trains Found Try to change Stations";
                    } else {
                        // Train found, display the results
                        do {
                            int trainId = resultSet.getInt("TrainID");
                            String trainName = resultSet.getString("TrainName");
                            String trainType = resultSet.getString("TrainType");
                            int totalSeats = resultSet.getInt("TotalSeats");
                            String sourceStations = resultSet.getString("SourceStations");
                            String destinationStationResult = resultSet.getString("DestinationStation");
                            Time departureTime = resultSet.getTime("DepartureTime");
                            Time arrivalTime = resultSet.getTime("ArrivalTime");
                            List<String> stations = getStationsForTrain(trainId,connection);
                            Train train = new Train(trainId,trainName,trainType,totalSeats,sourceStations,destinationStationResult,departureTime,arrivalTime,stations);
                            trainList.add(train);
                        } while (resultSet.next());
                    }
                }
                Gson gson = new Gson();
                jSonData = gson.toJson(trainList);

        } catch (SQLException e) {
            return "An error occurred while searching for trains. Please try again later.";
        }
        return jSonData;
    }
    // Helper method to fetch and print the list of stations for a specific train
    private List<String> getStationsForTrain(int trainId, Connection connection) {
        // SQL query to get the station names for a specific train, ordered by the station order
        String query = "SELECT s.StationName " +
                "FROM StationDetails s " +
                "JOIN TrainStation ts ON s.StationID = ts.StationID " +
                "WHERE ts.TrainID = ? " +
                "ORDER BY ts.StationOrder"; // Ensure stations are printed in the correct order
        List<String> stations = new ArrayList<>();

        // Try-with-resources to automatically close the PreparedStatement and ResultSet
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the train ID parameter for the query
            preparedStatement.setInt(1, trainId);

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String stationName = resultSet.getString("StationName");
                 stations.add(stationName);
            }
        } catch (SQLException e) {
            // In case of an error while fetching the stations, print a user-friendly message
            stations.add("An error occurred while retrieving stations for the train. Please try again later.");
        }
        return stations;
    }
    public boolean isTrainIdValid(int trainId) {
        // SQL query to check if the train ID exists in the TrainDetails table
        String query = "SELECT COUNT(*) FROM traindetails WHERE TrainID = ?";


        try (Connection connection = createConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set the train ID parameter
            stmt.setInt(1, trainId);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Check if the count is greater than 0 (i.e., train ID exists)
                int count = rs.getInt(1);
                return count > 0;
            } else {
                System.out.println("No train found for Train ID: " + trainId);
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while validating the Train ID. Please try again later.");
        }
        return false;
    }

    private static final List<String> stationList = new ArrayList<>();

    public void loadStationsFromDB() {
        stationList.clear();
        try (Connection con = createConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT SourceStations FROM traindetail SELECT DISTINCT DestinationStation FROM traindetails")) {

            while (rs.next()) {
                stationList.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}