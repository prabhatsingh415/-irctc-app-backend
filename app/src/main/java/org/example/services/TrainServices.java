package org.example.services;

import java.sql.*;

import static org.example.dataBase.DataBaseConfig.createConnection;

public class TrainServices {

    // Method to search for trains based on the arrival and destination stations
    public void searchTrain(String arrivalStation, String destinationStation) {
        // SQL query to find trains based on the arrival and destination stations,
        // ensuring that the arrival station comes before the destination station
        String query = "SELECT DISTINCT t.TrainID, t.TrainName, t.TrainType, t.TotalSeats, " +
                "t.SourceStations, t.DestinationStation, t.DepartureTime, t.ArrivalTime " +
                "FROM TrainDetails t " +
                "JOIN TrainStation tsArrival ON t.TrainID = tsArrival.TrainID " +
                "JOIN StationDetails sArrival ON tsArrival.StationID = sArrival.StationID " +
                "JOIN TrainStation tsDestination ON t.TrainID = tsDestination.TrainID " +
                "JOIN StationDetails sDestination ON tsDestination.StationID = sDestination.StationID " +
                "WHERE LOWER(sArrival.StationName) = LOWER(?) " +  // Match the arrival station name (case-insensitive)
                "AND LOWER(sDestination.StationName) = LOWER(?) " + // Match the destination station name (case-insensitive)
                "AND tsArrival.StationOrder < tsDestination.StationOrder " + // Ensure the flow of stations is correct
                "AND tsArrival.StationOrder < (SELECT MAX(ts.StationOrder) " +
                "                             FROM TrainStation ts " +
                "                             WHERE ts.TrainID = t.TrainID)"; // Arrival station should not be the last station

        // Try-with-resources to automatically close resources (Connection, PreparedStatement, ResultSet)
        try (Connection connection = createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters for the query (arrival station and destination station)
            preparedStatement.setString(1, arrivalStation.trim());
            preparedStatement.setString(2, destinationStation.trim());

            // Execute the query and retrieve the results
            ResultSet resultSet = preparedStatement.executeQuery();

            // If no results are found, inform the user
            if (!resultSet.next()) {
                System.out.println("No results found for the given stations.");
            } else {
                // Iterate over all the result rows (train details)
                do {
                    // Retrieve train details from the result set
                    int trainId = resultSet.getInt("TrainID");
                    String trainName = resultSet.getString("TrainName");
                    String trainType = resultSet.getString("TrainType");
                    int totalSeats = resultSet.getInt("TotalSeats");
                    String sourceStations = resultSet.getString("SourceStations");
                    String destinationStationResult = resultSet.getString("DestinationStation");
                    Time departureTime = resultSet.getTime("DepartureTime");
                    Time arrivalTime = resultSet.getTime("ArrivalTime");

                    // Print the train details to the user
                    System.out.println("Train ID: " + trainId);
                    System.out.println("Train Name: " + trainName);
                    System.out.println("Train Type: " + trainType);
                    System.out.println("Total Seats: " + totalSeats);
                    System.out.println("Source Stations: " + sourceStations);
                    System.out.println("Destination Station: " + destinationStationResult);
                    System.out.println("Departure Time: " + departureTime);
                    System.out.println("Arrival Time: " + arrivalTime);

                    // Fetch and display all stations for the given train
                    System.out.print("Stations: { ");
                    printStationsForTrain(trainId, connection);  // Call method to print stations for this train
                    System.out.println(" }");
                    System.out.println("------------------------------------------------");
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            // In case of an exception, print a user-friendly message
            System.out.println("An error occurred while searching for trains. Please try again later.");
        }
    }

    // Helper method to fetch and print the list of stations for a specific train
    private void printStationsForTrain(int trainId, Connection connection) {
        // SQL query to get the station names for a specific train, ordered by the station order
        String query = "SELECT s.StationName " +
                "FROM StationDetails s " +
                "JOIN TrainStation ts ON s.StationID = ts.StationID " +
                "WHERE ts.TrainID = ? " +
                "ORDER BY ts.StationOrder"; // Ensure stations are printed in the correct order

        // Try-with-resources to automatically close the PreparedStatement and ResultSet
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the train ID parameter for the query
            preparedStatement.setInt(1, trainId);

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Boolean to handle the formatting of station names (to manage commas between stations)
            boolean firstStation = true;
            while (resultSet.next()) {
                String stationName = resultSet.getString("StationName");
                if (!firstStation) {
                    System.out.print(", ");
                }
                System.out.print(stationName);
                firstStation = false; // After the first station, set the flag to false
            }
        } catch (SQLException e) {
            // In case of an error while fetching the stations, print a user-friendly message
            System.out.println("An error occurred while retrieving stations for the train. Please try again later.");
        }
    }
}

