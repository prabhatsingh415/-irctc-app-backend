package org.example.entities;

import java.util.Date;
import java.util.List;

public class Train {
    private int trainId;
    private String trainName;
    private String trainType; // e.g., Express, Local, etc.
    private int totalSeats;
    private List<String> route; // List of station names in the route
    private String sourceStation;
    private String destinationStation;
    private Date departureTime; // e.g., "08:30 AM"

    public Train() {
    }

    private Date arrivalTime;   // e.g., "05:45 PM"
    private double ticketPrice;   // Basic ticket price for this train

    // Constructor
    public Train(int trainId, String trainName, String trainType, int totalSeats,
                 List<String> route, String sourceStation, String destinationStation,
                 Date departureTime, Date arrivalTime, double ticketPrice) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.trainType = trainType;
        this.totalSeats = totalSeats;
        this.route = route;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.ticketPrice = ticketPrice;
    }

    // Getters and Setters
    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public void setSourceStation(String sourceStation) {
        this.sourceStation = sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    // Optional: Additional methods
    public boolean isStationOnRoute(String station) {
        return route.contains(station);
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + trainId + '\'' +
                ", trainName='" + trainName + '\'' +
                ", trainType='" + trainType + '\'' +
                ", totalSeats=" + totalSeats +
                ", sourceStation='" + sourceStation + '\'' +
                ", destinationStation='" + destinationStation + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
