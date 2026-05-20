package com.example.FastX.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RouteResponseDTO {

    private int routeId;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int durationInMinutes;
    private boolean assigned;

    private String busName;
    private String busNumber;
    private String operatorName;
}