package com.example.FastX.service;

import com.example.FastX.dto.BookingsResponseDTO;
import com.example.FastX.dto.OperatorDTO;
import com.example.FastX.dto.PassengerDTO;
import com.example.FastX.dto.RouteResponseDTO;
import com.example.FastX.exception.BadRequestException;
import com.example.FastX.exception.ResourceNotFoundException;

import java.util.List;

public interface AdminService {

    List<PassengerDTO> getAllPassengers();
    String deletePassenger(int id) throws ResourceNotFoundException, BadRequestException;

    List<OperatorDTO> getAllOperators();
    String deleteOperator(int id) throws ResourceNotFoundException, BadRequestException;

    List<RouteResponseDTO> getAllRoutes();
    String deleteRoute(int id) throws ResourceNotFoundException, BadRequestException;

    List<BookingsResponseDTO> getAllBookings();
    BookingsResponseDTO getBookingById(int id) throws ResourceNotFoundException, BadRequestException;
    String deleteBooking(int id) throws ResourceNotFoundException, BadRequestException;
}