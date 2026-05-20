package com.example.FastX.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RouteSearchResponseDTO {
    private int busId;
    private String busName;
    private String busNumber;
    private String busCategory;
    private boolean ac;
    private boolean waterBottle;
    private boolean blanket;
    private boolean tv;
    private boolean chargingPoint;
    private boolean sleeper;
    private int noOfSeats;

    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int durationInMinutes;
    private double fare;
    private int availableSeats;
}