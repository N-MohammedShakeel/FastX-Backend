package com.example.FastX.dto;

import com.example.FastX.constants.RefundStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RefundRequestDTO {

    @Positive(message = "bookingId must be positive")
    private int bookingId;

    private RefundStatus status;
}