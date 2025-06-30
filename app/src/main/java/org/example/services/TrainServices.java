package org.example.services;

import com.google.gson.Gson;
import org.example.entities.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.DataBaseConfig.createConnection;

public class TrainServices {

    private static final List<String> stationList = new ArrayList<>();

    public TrainServices() {
        loadStationsFromDB(); // Load stations once at service start
    }

    // Search trains by arrival and destination stations; returns JSON string or message
    public String searchTrain(String arrivalStation, String destinationStation) {
        List<Train> trainList = new ArrayList<>();
        String jsonData;

        String query = """
                (
                SELECT DISTINCT t.TrainID, t.TrainName, t.TrainType, t.TotalSeats,
                t.SourceStations, t.DestinationStation, t.DepartureTime, t.ArrivalTime
                FROM traindetails t
                WHERE LOWER(t.SourceStations) = LOWER(?)
                AND LOWER(t.DestinationStation) = LOWER(?)
        )
                UNION
                        (
                                SELECT DISTINCT t.TrainID, t.TrainName, t.TrainType, t.TotalSeats,
                                t.SourceStations, t.DestinationStation, t.DepartureTime, t.ArrivalTime
                                FROM traindetails t
                                JOIN trainstation tsArrival ON t.TrainID = tsArrival.TrainID
                                JOIN stationdetails sArrival ON tsArrival.StationID = sArrival.StationID
                                JOIN trainstation tsDestination ON t.TrainID = tsDestination.TrainID
                                JOIN stationdetails sDestination ON tsDestination.StationID = sDestination.StationID
                                WHERE LOWER(sArrival.StationName) = LOWER(?)
                AND LOWER(sDestination.StationName) = LOWER(?)
                AND tsArrival.StationOrder < tsDestination.StationOrder
          )
           """;

        try (Connection connection = createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, arrivalStation.trim());  // for direct match (query part 1)
            preparedStatement.setString(2, destinationStation.trim()); // for direct match (query part 1)
            preparedStatement.setString(3, arrivalStation.trim());  // for intermediate check (query part 2)
            preparedStatement.setString(4, destinationStation.trim()); // for intermediate check (query part 2)


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return "No Trains Found. Try changing stations.";
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

                        // Fetch stations for this train
                        List<String> stations = getStationsForTrain(trainId, connection);

                        Train train = new Train(trainId, trainName, trainType, totalSeats,
                                sourceStations, destinationStationResult, departureTime, arrivalTime, stations);
                        trainList.add(train);
                    } while (resultSet.next());
                }
            }

            jsonData = new Gson().toJson(trainList);
        } catch (SQLException e) {
            return "An error occurred while searching for trains. Please try again later.";
        }

        return jsonData;
    }

    // Get list of stations for a given train, ordered by station order
    private List<String> getStationsForTrain(int trainId, Connection connection) {
        String query = "SELECT s.StationName " +
                "FROM stationdetails s " +
                "JOIN trainstation ts ON s.StationID = ts.StationID " +
                "WHERE ts.TrainID = ? " +
                "ORDER BY ts.StationOrder";

        List<String> stations = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, trainId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    stations.add(resultSet.getString("StationName"));
                }
            }
        } catch (SQLException e) {
            // Instead of adding error message as a station, better to log and return empty or partial list
            e.printStackTrace();
        }

        return stations;
    }

    // Validate if a train ID exists
    public boolean isTrainIdValid(int trainId) {
        String query = "SELECT COUNT(*) FROM traindetails WHERE TrainID = ?";

        try (Connection connection = createConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, trainId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Load all unique stations from the DB into a static list
    public void loadStationsFromDB() {
        stationList.clear();

        String query = "SELECT DISTINCT SourceStations FROM traindetails " +
                "UNION " +
                "SELECT DISTINCT DestinationStation FROM traindetails";

        try (Connection connection = createConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                stationList.add(rs.getString(1));
            }
        } catch (SQLException e) {
              e.printStackTrace();
        }
    }

    public List<String> getStationsForTrain(int trainId) {
        List<String> stations = new ArrayList<>();
        String query = "SELECT SourceStations, DestinationStation FROM traindetails WHERE TrainId = ?";

        try (Connection conn = createConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, trainId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String source = rs.getString("SourceStations");
                String dest = rs.getString("DestinationStation");

                if (source != null && !source.isEmpty() && !stations.contains(source)) {
                    stations.add(source);
                }
                if (dest != null && !dest.isEmpty() && !stations.contains(dest)) {
                    stations.add(dest);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }

    public static List<String> getStationList() {
        return new ArrayList<>(stationList); // return copy for safety
    }
}
