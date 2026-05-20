package com.example.FastX.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {

    @Schema(
            description = "Error code representing the error happened"
    )
    private int status;

    @Schema(
            description = "Error message representing the error happened"
    )
    private Object message;

    @Schema(
            description = "Time representing when the error happened"
    )
    private LocalDateTime timestamp;

    public ErrorResponseDTO(int status, Object message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}