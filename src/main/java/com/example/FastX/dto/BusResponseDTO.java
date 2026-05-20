package com.example.FastX.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BusResponseDTO {

    private int busId;
    private String busNumber;
    private String name;
    private String busCategory;
    private int noOfSeats;
    private double fare;
    private boolean ac;
    private boolean sleeper;
    private boolean waterBottle;
    private boolean blanket;
    private boolean tv;
    private boolean chargingPoint;

    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int routeId;

    private int seatsLeft;
}