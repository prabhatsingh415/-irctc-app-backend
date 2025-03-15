package org.example.entities;

import java.sql.Time;
import java.util.List;

public class Train {
    private final int trainId;
    private final String trainName;
    private final String trainType;
    private final int totalSeats;
    private final String sourceStation;
    private final String destinationStation;
    private final Time departureTime;
    private final Time arrivalTime;
    private final List<String> stations; // New field for storing station names

    // Constructor
    public Train(int trainId, String trainName, String trainType, int totalSeats,
                 String sourceStation, String destinationStation, Time departureTime,
                 Time arrivalTime, List<String> stations) {  // Added stations parameter
        this.trainId = trainId;
        this.trainName = trainName;
        this.trainType = trainType;
        this.totalSeats = totalSeats;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.stations = stations; // Assigning stations list
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId=" + trainId +
                ", trainName='" + trainName + '\'' +
                ", trainType='" + trainType + '\'' +
                ", totalSeats=" + totalSeats +
                ", sourceStation='" + sourceStation + '\'' +
                ", destinationStation='" + destinationStation + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", stations=" + stations + // Include stations in toString()
                '}';
    }
}
