package org.example.services;

import java.sql.*;
import java.util.Scanner;

import static org.example.dataBase.DataBaseConfig.createConnection;

public class TrainServices {

    // Method to search for trains based on the arrival and destination stations
    public void searchTrain(String arrivalStation, String destinationStation) {
        boolean trainFound = false; // Flag to track if a train is found

        try (Connection connection = createConnection()) {
            // Loop until a train is found or the user decides to exit
            while (!trainFound) {
                // SQL query to find trains
                String query = "SELECT DISTINCT t.TrainID, t.TrainName, t.TrainType, t.TotalSeats, " +
                        "t.SourceStations, t.DestinationStation, t.DepartureTime, t.ArrivalTime " +
                        "FROM TrainDetails t " +
                        "JOIN TrainStation tsArrival ON t.TrainID = tsArrival.TrainID " +
                        "JOIN StationDetails sArrival ON tsArrival.StationID = sArrival.StationID " +
                        "JOIN TrainStation tsDestination ON t.TrainID = tsDestination.TrainID " +
                        "JOIN StationDetails sDestination ON tsDestination.StationID = sDestination.StationID " +
                        "WHERE LOWER(sArrival.StationName) = LOWER(?) " +
                        "AND LOWER(sDestination.StationName) = LOWER(?) " +
                        "AND tsArrival.StationOrder < tsDestination.StationOrder " +
                        "AND tsArrival.StationOrder < (SELECT MAX(ts.StationOrder) " +
                        "                             FROM TrainStation ts " +
                        "                             WHERE ts.TrainID = t.TrainID)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    // Set query parameters
                    preparedStatement.setString(1, arrivalStation.trim());
                    preparedStatement.setString(2, destinationStation.trim());

                    // Execute the query
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (!resultSet.next()) {
                        // No results found, prompt the user to re-enter stations
                        System.out.println("No trains found for the given stations.");
                        System.out.println("Do you want to try again? (yes/no): ");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine().trim().toLowerCase();

                        if (choice.equals("no")) {
                            System.out.println("Exiting search.");
                            return; // Exit the method
                        }

                        // Prompt the user to re-enter the stations
                        System.out.print("Enter arrival station: ");
                        arrivalStation = scanner.nextLine();
                        System.out.print("Enter destination station: ");
                        destinationStation = scanner.nextLine();
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

                            System.out.println("Train ID: " + trainId);
                            System.out.println("Train Name: " + trainName);
                            System.out.println("Train Type: " + trainType);
                            System.out.println("Total Seats: " + totalSeats);
                            System.out.println("Source Stations: " + sourceStations);
                            System.out.println("Destination Station: " + destinationStationResult);
                            System.out.println("Departure Time: " + departureTime);
                            System.out.println("Arrival Time: " + arrivalTime);

                            System.out.print("Stations: { ");
                            printStationsForTrain(trainId, connection);
                            System.out.println(" }");
                            System.out.println("------------------------------------------------");
                        } while (resultSet.next());
                        trainFound = true; // Set flag to true to exit the loop
                    }
                }
            }
        } catch (SQLException e) {
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
    public boolean isTrainIdValid(int trainId) {
        // SQL query to check if the train ID exists in the TrainDetails table
        String query = "SELECT COUNT(*) FROM TrainDetails WHERE TrainID = ?";

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

}

