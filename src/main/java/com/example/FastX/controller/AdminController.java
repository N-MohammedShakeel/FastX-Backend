package com.example.FastX.controller;

import com.example.FastX.exception.BadRequestException;
import com.example.FastX.exception.ResourceNotFoundException;
import com.example.FastX.dto.ApiResponseDTO;
import com.example.FastX.service.Impl.AdminServiceImpl;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@Validated
public class AdminController {

    private AdminServiceImpl adminService;

    // Users
    @GetMapping("/passengers")
    public ResponseEntity<ApiResponseDTO> getAllPassengers() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Passengers fetched successfully",
                        adminService.getAllPassengers()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<ApiResponseDTO> deletePassenger(
            @PathVariable @Positive(message = "id must be positive") int id
    ) throws ResourceNotFoundException, BadRequestException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Passenger deleted successfully",
                        adminService.deletePassenger(id)),
                HttpStatus.OK
        );
    }

    // Operators
    @GetMapping("/operators")
    public ResponseEntity<ApiResponseDTO> getAllOperators() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Operators fetched successfully",
                        adminService.getAllOperators()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/operators/{id}")
    public ResponseEntity<ApiResponseDTO> deleteOperator(
            @PathVariable @Positive(message = "id must be positive") int id
    ) throws ResourceNotFoundException, BadRequestException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Operator deleted successfully",
                        adminService.deleteOperator(id)),
                HttpStatus.OK
        );
    }

    // Routes
    @GetMapping("/routes")
    public ResponseEntity<ApiResponseDTO> getAllRoutes() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Routes fetched successfully",
                        adminService.getAllRoutes()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<ApiResponseDTO> deleteRoute(
            @PathVariable @Positive(message = "id must be positive") int id
    ) throws ResourceNotFoundException, BadRequestException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Route deleted successfully",
                        adminService.deleteRoute(id)),
                HttpStatus.OK
        );
    }

    // Bookings
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponseDTO> getAllBookings() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Bookings fetched successfully",
                        adminService.getAllBookings()),
                HttpStatus.OK
        );
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<ApiResponseDTO> getBooking(
            @PathVariable @Positive(message = "id must be positive") int id
    ) throws ResourceNotFoundException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Booking fetched successfully",
                        adminService.getBookingById(id)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<ApiResponseDTO> deleteBooking(
            @PathVariable @Positive(message = "id must be positive") int id
    ) throws ResourceNotFoundException, BadRequestException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Booking deleted successfully",
                        adminService.deleteBooking(id)),
                HttpStatus.OK
        );
    }
}