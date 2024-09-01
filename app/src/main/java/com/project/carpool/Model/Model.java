package com.project.carpool.Model;

import java.io.Serializable;

public class Model implements Serializable {
    private String sourceAddress;
    private String destinationAddress;
    private String rideDate;
    private String rideTime;
    private String ridePrice;
    private String riderName;
    private String totalPassengers;
    private String userId;
    private String phoneNumber;
    private String vehicleNumber;
    private String vehicleModel;
    private String vehicleColor;
    private double sourceLatitude; // Add latitude for source
    private double sourceLongitude; // Add longitude for source
    private double destLatitude; // Add latitude for destination
    private double destLongitude;

    public Model() {
        // Default constructor required for Firebase
    }

    public Model(String sourceAddress, String destinationAddress, String rideDate, String rideTime, String ridePrice,
                 String riderName, String totalPassengers, String userId, String phoneNumber,
                 String vehicleNumber, String vehicleModel, String vehicleColor) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.rideDate = rideDate;
        this.rideTime = rideTime;
        this.ridePrice = ridePrice;
        this.riderName = riderName;
        this.totalPassengers = totalPassengers;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.vehicleColor = vehicleColor;
    }

    // Getters and setters for all fields

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public String getRideTime() {
        return rideTime;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

    public String getRidePrice() {
        return ridePrice;
    }

    public void setRidePrice(String ridePrice) {
        this.ridePrice = ridePrice;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(String totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public double getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(double sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public double getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(double sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(double destLongitude) {
        this.destLongitude = destLongitude;
    }
}
