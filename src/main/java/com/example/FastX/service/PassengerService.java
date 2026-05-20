package com.example.FastX.service;

import com.example.FastX.dto.*;
import com.example.FastX.exception.ResourceNotFoundException;
import jakarta.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;

public interface PassengerService {

    PassengerDTO getProfile();
    PassengerDTO updateProfile(UserUpdateDTO dto);
    List<RouteSearchResponseDTO> searchRoutes(String origin, String destination, LocalDate date);
    List<RouteSearchResponseDTO> getAllRoutes();
    List<Integer> getAvailableSeats(int busId) throws ResourceNotFoundException;

    BookingsResponseDTO bookTicket(BookingRequestDTO dto) throws ResourceNotFoundException, MessagingException; // deduct money from user wallet
    List<BookingsResponseDTO> getAllBookings();
    List<BookingsResponseDTO> getActiveBookings();
    List<BookingsResponseDTO> getPastBookings();

    BookingsResponseDTO getBookingById(int id) throws ResourceNotFoundException;
    void requestRefund(int bookingId) throws ResourceNotFoundException;

    double addMoney(double amount);
    void updatePassword(String oldPassword, String newPassword);
}