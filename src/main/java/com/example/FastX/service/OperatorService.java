package com.example.FastX.service;

import com.example.FastX.dto.*;
import com.example.FastX.exception.ResourceNotFoundException;

import java.util.List;

public interface OperatorService {

    OperatorDTO getProfile();
    OperatorDTO updateProfile(UserUpdateDTO dto);

    List<BusResponseDTO> getOperatorBuses();
    BusResponseDTO addBus(BusRequestDTO dto) throws ResourceNotFoundException;
    BusResponseDTO updateBus(int id, BusRequestDTO dto) throws ResourceNotFoundException;
    String deleteBus(int id) throws ResourceNotFoundException;

    List<RouteResponseDTO> getRoutes();
    RouteResponseDTO addRoute(RouteRequestDTO dto);
    RouteResponseDTO updateRoute(int id, RouteRequestDTO dto) throws ResourceNotFoundException;
    String deleteRoute(int id);

    List<BookingsResponseDTO> getBookings();
    BookingsResponseDTO getBookingById(int id) throws ResourceNotFoundException;

    List<RefundResponseDTO> getRefundRequests();
    RefundResponseDTO processRefund(int id, RefundRequestDTO dto) throws ResourceNotFoundException;

    void updatePassword(String oldPassword, String newPassword);

    OperatorStatsDTO getStats();
    void cancelBooking(int bookingId) throws ResourceNotFoundException;
}