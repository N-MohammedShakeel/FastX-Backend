package com.example.FastX.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

@Data
public class BookingRequestDTO {
    @Positive(message = "busId must be positive")
    private int busId;

    @Positive(message = "totalFare must be positive")
    private double totalFare;

    @Positive(message = "totalNoOfSeats must be positive")
    private int totalNoOfSeats;

    @NotNull(message = "seatNumbers must not be empty")
    private List<Integer> seatNumbers;
}