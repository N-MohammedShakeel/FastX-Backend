package com.example.FastX.controller;

import com.example.FastX.dto.*;
import com.example.FastX.dto.ApiResponseDTO;
import com.example.FastX.exception.ResourceNotFoundException;
import com.example.FastX.service.Impl.OperatorServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operator")
@AllArgsConstructor
@Validated
public class BusOperatorController {

    @Autowired
    private OperatorServiceImpl operatorService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO> getProfile() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Operator profile fetched",
                        operatorService.getProfile()),
                HttpStatus.OK
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponseDTO> updateProfile(
            @Valid @RequestBody UserUpdateDTO dto
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Operator profile updated",
                        operatorService.updateProfile(dto)),
                HttpStatus.OK
        );
    }

    // Bus Management
    @GetMapping("/bus")
    public ResponseEntity<ApiResponseDTO> getBuses() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Buses fetched successfully",
                        operatorService.getOperatorBuses()),
                HttpStatus.OK
        );
    }

    @PostMapping("/bus")
    public ResponseEntity<ApiResponseDTO> addBus(
            @Valid @RequestBody BusRequestDTO dto
    ) throws ResourceNotFoundException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.CREATED.value(),
                        "Bus added successfully",
                        operatorService.addBus(dto)),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/bus/{id}")
    public ResponseEntity<ApiResponseDTO> updateBus(
            @PathVariable @Positive(message = "id must be positive") int id,
            @Valid @RequestBody BusRequestDTO dto
    ) throws ResourceNotFoundException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Bus updated successfully",
                        operatorService.updateBus(id, dto)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/bus/{id}")
    public ResponseEntity<ApiResponseDTO> deleteBus(
            @PathVariable @Positive(message = "id must be positive") int id
    ) throws ResourceNotFoundException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Bus deleted successfully",
                        operatorService.deleteBus(id)),
                HttpStatus.OK
        );
    }

    // Route Management
    @GetMapping("/routes")
    public ResponseEntity<ApiResponseDTO> getRoutes() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Routes fetched successfully",
                        operatorService.getRoutes()),
                HttpStatus.OK
        );
    }

    @PostMapping("/routes")
    public ResponseEntity<ApiResponseDTO> addRoute(
            @Valid @RequestBody RouteRequestDTO dto
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.CREATED.value(),
                        "Route added successfully",
                        operatorService.addRoute(dto)),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/routes/{id}")
    public ResponseEntity<ApiResponseDTO> updateRoute(
            @PathVariable @Positive(message = "id must be positive")  int id,
            @Valid @RequestBody RouteRequestDTO dto
    ) throws ResourceNotFoundException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Route updated successfully",
                        operatorService.updateRoute(id, dto)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<ApiResponseDTO> deleteRoute(
            @PathVariable @Positive(message = "id must be positive")  int id
    ) {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Route deleted successfully",
                        operatorService.deleteRoute(id)),
                HttpStatus.OK
        );
    }

    // Booking Management
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponseDTO> getBookings() {
        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Bookings fetched successfully",
                        operatorService.getBookings()),
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
                        operatorService.getBookingById(id)),
                HttpStatus.OK
        );
    }

    @PutMapping("/bookings/{id}/cancel")
    public ResponseEntity<ApiResponseDTO> cancelBooking(
            @PathVariable int id
    ) throws ResourceNotFoundException {

        operatorService.cancelBooking(id);
        return new ResponseEntity<>(
                new ApiResponseDTO(
                        HttpStatus.OK.value(),
                        "Booking cancelled successfully",
                        null
                ),
                HttpStatus.OK
        );
    }

    // Refund Handling
    @GetMapping("/refunds")
    public ResponseEntity<ApiResponseDTO> getRefundRequests() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Refund requests fetched successfully",
                        operatorService.getRefundRequests()),
                HttpStatus.OK
        );
    }

    @PutMapping("/refunds/{id}")
    public ResponseEntity<ApiResponseDTO> processRefund(
            @PathVariable @Positive(message = "id must be positive") int id,
            @Valid @RequestBody RefundRequestDTO dto
    ) throws ResourceNotFoundException {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Refund processed successfully",
                        operatorService.processRefund(id, dto)),
                HttpStatus.OK
        );
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponseDTO> updatePassword(
            @Valid @RequestBody PasswordDTO dto
    ) {

        operatorService.updatePassword(dto.getOldPassword(), dto.getNewPassword());
        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Password updated successfully",
                        null),
                HttpStatus.OK
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponseDTO> stats() {

        return new ResponseEntity<>(
                new ApiResponseDTO(HttpStatus.OK.value(),
                        "Operator stats fetched successfully",
                        operatorService.getStats()),
                HttpStatus.OK
        );
    }
}