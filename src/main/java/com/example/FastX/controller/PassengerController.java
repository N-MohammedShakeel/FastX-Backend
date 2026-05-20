package com.example.FastX.controller;

import com.example.FastX.dto.*;
import com.example.FastX.exception.ResourceNotFoundException;
import com.example.FastX.service.PassengerService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/passenger")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO> getProfile() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Profile fetched",
                        userService.getProfile()),
                HttpStatus.OK
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponseDTO> updateProfile(
            @Valid @RequestBody UserUpdateDTO dto
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Profile updated",
                        userService.updateProfile(dto)),
                HttpStatus.OK
        );
    }

    @GetMapping("/routes")
    public ResponseEntity<ApiResponseDTO> getAllRoutes() {

        return new ResponseEntity<>(
                new ApiResponseDTO(
                        HttpStatus.OK.value(),
                        "Routes fetched",
                        userService.getAllRoutes()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/routes/search")
    public ResponseEntity<ApiResponseDTO> searchRoutes(
            @NotBlank(message = "origin must not be empty") @RequestParam String origin,
            @NotBlank(message = "destination must not be empty") @RequestParam String destination,
            @NotNull(message = "date must not be empty") @RequestParam LocalDate date
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Routes fetched",
                        userService.searchRoutes(origin, destination, date)),
                HttpStatus.OK
        );
    }

    @GetMapping("/bus/{busId}/seats")
    public ResponseEntity<ApiResponseDTO> getAvailableSeats(
            @Positive(message = "busId must be positive") @PathVariable int busId
    ) throws ResourceNotFoundException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Seats fetched",
                        userService.getAvailableSeats(busId)),
                HttpStatus.OK
        );
    }

    @PostMapping("/bookings")
    public ResponseEntity<ApiResponseDTO> bookTicket(
            @Valid @RequestBody BookingRequestDTO dto
    ) throws ResourceNotFoundException, MessagingException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Booking successful",
                        userService.bookTicket(dto)),
                HttpStatus.OK
        );
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponseDTO> getUserBookings() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Bookings fetched",
                        userService.getAllBookings()),
                HttpStatus.OK
        );
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiResponseDTO> getBooking(
            @PathVariable @Positive(message = "id must be positive") int id
    ) throws ResourceNotFoundException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Booking fetched",
                        userService.getBookingById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping("/bookings/active")
    public ResponseEntity<ApiResponseDTO> getActiveBookings() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Active bookings fetched",
                        userService.getActiveBookings()),
                HttpStatus.OK
        );
    }

    @GetMapping("/bookings/past")
    public ResponseEntity<ApiResponseDTO> getPastBookings() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Past bookings fetched",
                        userService.getPastBookings()),
                HttpStatus.OK
        );
    }

    @PostMapping("/refunds/{bookingId}")
    public ResponseEntity<ApiResponseDTO> requestRefund(
            @PathVariable @Positive(message = "id must be positive") int bookingId
    ) throws ResourceNotFoundException {

        userService.requestRefund(bookingId);
        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Refund requested",
                        null),
                HttpStatus.OK
        );
    }

    @PutMapping("/wallet/add")
    public ResponseEntity<ApiResponseDTO> addMoney(
            @RequestParam @Positive(message = "amount must be positive") double amount
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Money added successfully",
                        userService.addMoney(amount)),
                HttpStatus.OK
        );
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponseDTO> updatePassword(
            @Valid @RequestBody PasswordDTO dto
    ) {

        userService.updatePassword(dto.getOldPassword(), dto.getNewPassword());
        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Password updated successfully",
                        null),
                HttpStatus.OK
        );
    }
}