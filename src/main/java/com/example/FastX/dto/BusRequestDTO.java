package com.example.FastX.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BusRequestDTO {
    @NotBlank(message = "Bus number must not be empty")
    private String busNumber;

    @NotBlank(message = "Name must not be empty")
    private String name;

    @Positive(message = "Number of seats must be positive")
    private int noOfSeats;

    @Positive(message = "Fare must be positive")
    private double fare;

    @NotBlank(message = "Bus category must not be empty")
    private String busCategory;

    @NotNull(message = "AC option must not be null")
    private boolean ac;

    private boolean waterBottle;
    private boolean blanket;
    private boolean tv;
    private boolean chargingPoint;
    private boolean sleeper;

    @Positive(message = "Route ID must be positive")
    private int routeId;
}