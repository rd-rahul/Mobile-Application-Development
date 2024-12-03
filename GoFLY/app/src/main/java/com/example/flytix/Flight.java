package com.example.flytix;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Flight implements Serializable {
    private String airlineLogo;   // URL or path to airline logo image
    private String from;          // Departure location
    private String to;            // Destination location
    private String date;          // Flight date
    private String arriveTime;    // Arrival time
    private String fromShort;
    private String toShort;
    private String classSeat;     // Seat class (e.g., Economy, Business)
    private double price;         // Flight price per seat
    private String reservedSeats; // Comma-separated string of reserved seats
    private Set<String> reservedSeatSet; // Parsed set of reserved seats
    private int numberSeat;       // Total number of seats
    private String time;
    private String fromLocation;
    private String toLocation;
    // Default constructor for Firebase
    public Flight() {}

    // Getters and setters
    public String getAirlineLogo() {
        return airlineLogo;
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogo = airlineLogo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getFromShort() {
        return fromShort;
    }

    public void setFromShort(String fromShort) {
        this.fromShort = fromShort;
    }

    public String getToShort() {
        return toShort;
    }

    public void setToShort(String toShort) {
        this.toShort = toShort;
    }

    public String getClassSeat() {
        return classSeat;
    }

    public void setFromLocation(String fromLocation){this.fromLocation=fromLocation;}

    public String getFromLocation(){return fromLocation;}

    public void setToLocation(String toLocation){this.toLocation=toLocation;}

    public String getToLocation(){return toLocation;}

    public void setClassSeat(String classSeat) {
        this.classSeat = classSeat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberSeat() {
        return numberSeat;
    }

    public void setNumberSeat(int numberSeat) {
        this.numberSeat = numberSeat;
    }


    public String getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(String reservedSeats) {
        this.reservedSeats = reservedSeats;
        this.reservedSeatSet = new HashSet<>(Arrays.asList(reservedSeats.split(",")));
    }

    public Set<String> getReservedSeatSet() {
        return reservedSeatSet != null ? reservedSeatSet : new HashSet<>();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
