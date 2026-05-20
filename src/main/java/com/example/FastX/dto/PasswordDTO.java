package com.example.FastX.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordDTO {

    @NotBlank(message = "old password should not be empty")
    private String oldPassword;

    @NotBlank(message = "new password should not be empty")
    private String newPassword;
}