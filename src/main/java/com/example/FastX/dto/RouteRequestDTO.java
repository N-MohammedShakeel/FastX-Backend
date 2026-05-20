package com.example.FastX.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RouteRequestDTO {

    @NotBlank(message = "origin should not be empty")
    private String origin;

    @NotBlank(message = "destination should not be empty")
    private String destination;

    @NotNull(message = "departure time should not be empty")
    private LocalDateTime departureTime;

    @Positive(message = "duration should be positive")
    private int durationInMinutes;
}