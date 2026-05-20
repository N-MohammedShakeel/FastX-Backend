package com.example.FastX.dto;

import com.example.FastX.constants.AuthProvider;
import com.example.FastX.constants.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class OperatorDTO {
    private int id;

    @NotBlank(message = "name should not be empty")
    private String name;

    @NotBlank(message = "email should not be empty")
    @Email(message = "email is invalid")
    private String email;

    @NotBlank(message = "phone should not be empty")
    @Pattern(regexp = "\\d{10}", message = "phone number must have 10 digits")
    private String phone;

    @NotBlank(message = "gender should not be empty")
    private String gender;

    @NotBlank(message = "address should not be empty")
    private String address;

    @Min(value = 0, message = "wallet should not be negative")
    private double wallet;

    private AuthProvider provider;
    private Role role;
    private boolean active;

    private boolean passwordChanged;

    private List<BusResponseDTO> buses;
    private List<RefundResponseDTO> refunds;
}