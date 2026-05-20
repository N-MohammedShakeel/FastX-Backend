package com.example.FastX.dto;

import com.example.FastX.constants.RefundStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RefundResponseDTO {

    private int refundId;
    private double amount;

    private RefundStatus status;

    private int bookingId;
    private LocalDateTime bookingTime;
    private String busNumber;

    private String passengerName;
}