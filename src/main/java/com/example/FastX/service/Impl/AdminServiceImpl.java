package com.example.FastX.service.Impl;

import com.example.FastX.constants.BookingStatus;
import com.example.FastX.constants.RefundStatus;
import com.example.FastX.constants.Role;
import com.example.FastX.dto.BookingsResponseDTO;
import com.example.FastX.dto.OperatorDTO;
import com.example.FastX.dto.RouteResponseDTO;
import com.example.FastX.service.AdminService;
import com.example.FastX.util.Mapper;
import com.example.FastX.dto.PassengerDTO;
import com.example.FastX.entity.*;
import com.example.FastX.exception.BadRequestException;
import com.example.FastX.exception.ResourceNotFoundException;
import com.example.FastX.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final BookingRepository bookingRepository;
    private final BusRepository busRepository;
    private final RefundRepository refundRepository;

    @Override
    public List<PassengerDTO> getAllPassengers() {
        return userRepository.findByRole(Role.PASSENGER).stream().map(Mapper::toPassengerDTO).toList();
    }

    @Override
    public String deletePassenger(int id) throws ResourceNotFoundException, BadRequestException {

        User passenger = userRepository.findByIdAndRole(id, Role.PASSENGER);

        if (passenger == null){
            throw new ResourceNotFoundException("Passenger not found");
        }

        if (bookingRepository.existsByPassenger_IdAndStatus(id,BookingStatus.BOOKED)) {
            throw new BadRequestException("Passenger has active bookings. Cannot delete.");
        }

        if (refundRepository.existsByPassenger_IdAndStatus(id, RefundStatus.PENDING)) {
            throw new BadRequestException("Passenger has pending refunds. Cannot delete.");
        }

        passenger.setActive(false);
        userRepository.save(passenger);
        return "Passenger deactivated successfully";
    }

    @Override
    public List<OperatorDTO> getAllOperators() {
        return userRepository.findByRole(Role.OPERATOR).stream().map(Mapper::toOperatorDTO).toList();
    }

    @Override
    public String deleteOperator(int id) throws ResourceNotFoundException,BadRequestException {

        User operator = userRepository.findByIdAndRole(id, Role.OPERATOR);

        if (operator == null){
            throw new ResourceNotFoundException("Passenger not found");
        }
        if (!busRepository.findByOperator_Id(id).isEmpty()) {
            throw new BadRequestException("Operator has active buses. Cannot delete.");
        }

        if (refundRepository.existsByOperator_IdAndStatus(id, RefundStatus.PENDING)) {
            throw new BadRequestException("Operator has pending refunds. Cannot delete.");
        }

        operator.setActive(false);
        userRepository.save(operator);
        return "Operator deactivated successfully";
    }

    @Override
    public List<RouteResponseDTO> getAllRoutes() {
        return routeRepository.findAll().stream().map(Mapper::toRouteResponseDTO).toList();
    }

    @Override
    public String deleteRoute(int id) throws ResourceNotFoundException,BadRequestException {

        Route route = routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        Bus bus = busRepository.findByRoute_RouteId(id);
        if (bus != null) {
            throw new BadRequestException("Route is assigned to a bus. Cannot delete.");
        }

        routeRepository.delete(route);
        return "Route deleted successfully";
    }

    @Override
    public List<BookingsResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(Mapper::toBookingResponseDTO).toList();
    }

    @Override
    public BookingsResponseDTO getBookingById(int id) throws ResourceNotFoundException {

        return Mapper.toBookingResponseDTO(bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found")));
    }

    @Override
    public String deleteBooking(int id) throws ResourceNotFoundException, BadRequestException {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.BOOKED) {
            throw new BadRequestException("Active booking cannot be deleted. Cancel it first.");
        }

        if (booking.getRefund() != null && (RefundStatus.PENDING == booking.getRefund().getStatus())) {
            throw new BadRequestException("Booking has pending refund. Cannot delete.");
        }

        bookingRepository.delete(booking);
        return "Booking deleted successfully";
    }
}