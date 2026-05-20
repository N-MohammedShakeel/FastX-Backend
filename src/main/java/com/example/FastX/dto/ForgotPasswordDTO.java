package com.example.FastX.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordDTO {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email required")
    private String email;
}