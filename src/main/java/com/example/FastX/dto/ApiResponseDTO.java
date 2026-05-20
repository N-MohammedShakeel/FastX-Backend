package com.example.FastX.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApiResponseDTO {

    private int status;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    public ApiResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}