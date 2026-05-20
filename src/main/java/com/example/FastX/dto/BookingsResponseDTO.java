package com.example.FastX.dto;

import com.example.FastX.constants.BookingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingsResponseDTO {
    private int bookingId;
    private double totalFare;
    private int totalNoOfSeats;
    private LocalDateTime bookingTime;
    private BookingStatus status;

    private String busName;
    private String busNumber;

    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private List<Integer> seatNumbers;

    private String passengerName;
}