package com.example.FastX.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @NotBlank(message = "name should not be empty")
    private String name;

    @NotBlank(message = "phone should not be empty")
    @Pattern(regexp = "\\d{10}", message = "phone number must have 10 digits")
    private String phone;

    @NotBlank(message = "gender should not be empty")
    private String gender;

    @NotBlank(message = "address should not be empty")
    private String address;
}
