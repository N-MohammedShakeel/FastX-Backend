package com.example.FastX.service.Impl;

import com.example.FastX.constants.AuthProvider;
import com.example.FastX.constants.BookingStatus;
import com.example.FastX.constants.RefundStatus;
import com.example.FastX.dto.*;
import com.example.FastX.entity.*;
import com.example.FastX.exception.BadRequestException;
import com.example.FastX.exception.ResourceNotFoundException;
import com.example.FastX.repository.*;
import com.example.FastX.service.OperatorService;
import com.example.FastX.util.Mapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OperatorServiceImpl implements OperatorService {

    private final UserRepository userRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final BookingRepository bookingRepository;
    private final RefundRepository refundRepository;
    private final BookedSeatRepository seatRepository;


    private User getCurrentOperator() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndActiveTrue(email);
    }

    @Override
    public OperatorDTO getProfile() {
        return Mapper.toOperatorDTO(getCurrentOperator());
    }

    @Override
    public OperatorDTO updateProfile(UserUpdateDTO dto) {
        User operator = getCurrentOperator();

        operator.setName(dto.getName());
        operator.setPhone(dto.getPhone());
        operator.setAddress(dto.getAddress());

        return Mapper.toOperatorDTO(userRepository.save(operator));
    }


    @Override
    public BusResponseDTO addBus(BusRequestDTO dto) throws ResourceNotFoundException {

        User operator = getCurrentOperator();
        Route route = routeRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        if (route.getBus() != null) {
            throw new BadRequestException(
                    "Route already assigned to another bus"
            );
        }

        Bus bus = new Bus();
        bus.setBusNumber(dto.getBusNumber());
        bus.setName(dto.getName());
        bus.setBusCategory(dto.getBusCategory());
        bus.setNoOfSeats(dto.getNoOfSeats());
        bus.setFare(dto.getFare());
        bus.setAc(dto.isAc());
        bus.setWaterBottle(dto.isWaterBottle());
        bus.setBlanket(dto.isBlanket());
        bus.setTv(dto.isTv());
        bus.setChargingPoint(dto.isChargingPoint());
        bus.setSleeper(dto.isSleeper());

        bus.setRoute(route);
        bus.setOperator(operator);

        return Mapper.toBusResponseDTO(busRepository.save(bus));
    }

    @Override
    public BusResponseDTO updateBus(int id, BusRequestDTO dto) throws ResourceNotFoundException {

        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        Route route = routeRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        if (route.getBus() != null && route.getBus().getBusId() != bus.getBusId())
            throw new BadRequestException("Route already assigned to another bus");


        bus.setBusNumber(dto.getBusNumber());
        bus.setName(dto.getName());
        bus.setBusCategory(dto.getBusCategory());
        bus.setNoOfSeats(dto.getNoOfSeats());
        bus.setFare(dto.getFare());
        bus.setAc(dto.isAc());
        bus.setWaterBottle(dto.isWaterBottle());
        bus.setBlanket(dto.isBlanket());
        bus.setTv(dto.isTv());
        bus.setChargingPoint(dto.isChargingPoint());
        bus.setSleeper(dto.isSleeper());
        bus.setRoute(route);

        return Mapper.toBusResponseDTO(busRepository.save(bus));
    }

    @Override
    public String deleteBus(int id) throws ResourceNotFoundException {

        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        busRepository.delete(bus);
        return "Bus deleted successfully";
    }

    @Override
    public List<BusResponseDTO> getOperatorBuses() {
        return busRepository.findByOperator_Id(getCurrentOperator().getId())
                .stream()
                .map(Mapper::toBusResponseDTO)
                .toList();
    }

    @Override
    public RouteResponseDTO addRoute(RouteRequestDTO dto) {

        Route route = new Route();

        route.setOrigin(dto.getOrigin());
        route.setDestination(dto.getDestination());
        route.setDepartureTime(dto.getDepartureTime());
        route.setDurationInMinutes(dto.getDurationInMinutes());

        return Mapper.toRouteResponseDTO(routeRepository.save(route));
    }

    @Override
    public RouteResponseDTO updateRoute(int id, RouteRequestDTO dto) throws ResourceNotFoundException {

        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not Found"));

        route.setOrigin(dto.getOrigin());
        route.setDestination(dto.getDestination());
        route.setDepartureTime(dto.getDepartureTime());
        route.setDurationInMinutes(dto.getDurationInMinutes());

        return Mapper.toRouteResponseDTO(routeRepository.save(route));
    }

    @Override
    public String deleteRoute(int id) {

        routeRepository.deleteById(id);
        return "Route deleted successfully";
    }

    @Override
    public List<RouteResponseDTO> getRoutes() {
        return routeRepository.findAll().stream().map(Mapper::toRouteResponseDTO).toList();
    }

    @Override
    public List<BookingsResponseDTO> getBookings() {
        return bookingRepository.findByOperator_Id(getCurrentOperator().getId())
                .stream()
                .map(Mapper::toBookingResponseDTO)
                .toList();
    }

    @Override
    public BookingsResponseDTO getBookingById(int id) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return Mapper.toBookingResponseDTO(booking);
    }

    @Override
    public List<RefundResponseDTO> getRefundRequests() {
        return refundRepository.findByOperator_Id(getCurrentOperator().getId())
                .stream()
                .map(Mapper::toRefundResponseDTO)
                .toList();
    }

    @Transactional
    @Override
    public RefundResponseDTO processRefund(int id, RefundRequestDTO dto) throws ResourceNotFoundException {

        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found"));

        if (RefundStatus.PENDING != refund.getStatus()) {
            throw new BadRequestException("Refund already processed");
        }

        Booking booking = refund.getBooking();
        User operator = refund.getOperator();
        User user = refund.getPassenger();

        if (RefundStatus.APPROVED == dto.getStatus()) {

            if (operator.getWallet() < refund.getAmount()) {
                throw new BadRequestException("Insufficient operator balance");
            }

            operator.setWallet(operator.getWallet() - refund.getAmount());
            user.setWallet(user.getWallet() + refund.getAmount());

            booking.setStatus(BookingStatus.CANCELLED);
            refund.setStatus(RefundStatus.APPROVED);

            seatRepository.deleteByBooking_BookingId(
                    booking.getBookingId()
            );

        } else if (RefundStatus.REJECTED == dto.getStatus()) {

            booking.setStatus(BookingStatus.CONFIRMED);
            refund.setStatus(RefundStatus.REJECTED);

        } else {
            throw new BadRequestException("Invalid refund status");
        }

        return Mapper.toRefundResponseDTO(refundRepository.save(refund));
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {

        User operator = getCurrentOperator();

        if (newPassword == null || newPassword.isEmpty()) {
            throw new BadRequestException("Password must not be empty");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        if (AuthProvider.GOOGLE == operator.getProvider() && !operator.isPasswordChanged()) {
            operator.setPassword(encoder.encode(newPassword));
            operator.setPasswordChanged(true);
            userRepository.save(operator);
            return;
        }

        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new BadRequestException("Old password required");
        }

        if (!encoder.matches(oldPassword, operator.getPassword())) {
            throw new BadRequestException("Old password incorrect");
        }

        operator.setPassword(encoder.encode(newPassword));
        userRepository.save(operator);
    }

    @Override
    public OperatorStatsDTO getStats() {
        User operator = getCurrentOperator();
        int operatorId = operator.getId();

        List<Bus> buses = busRepository.findByOperator_Id(operatorId);
        List<Booking> bookings = bookingRepository.findByOperator_Id(operatorId);
        List<Refund> refunds = refundRepository.findByOperator_Id(operatorId);

        int totalBus = buses.size();
        int totalRoute = (int) buses.stream()
                .map(Bus::getRoute)
                .filter(Objects::nonNull)
                .count();

        int totalBooking = bookings.size();
        int totalRefund = refunds.size();

        double totalAmountRefunded = refunds.stream()
                .filter(refund -> RefundStatus.APPROVED == refund.getStatus())
                .mapToDouble(Refund::getAmount)
                .sum();

        double totalRevenue = bookings.stream()
                .filter(booking -> BookingStatus.BOOKED == booking.getStatus())
                .mapToDouble(Booking::getTotalFare)
                .sum();

        OperatorStatsDTO statsDTO = new OperatorStatsDTO();
        statsDTO.setTotalBus(totalBus);
        statsDTO.setTotalRoute(totalRoute);
        statsDTO.setTotalBooking(totalBooking);
        statsDTO.setTotalRefund(totalRefund);
        statsDTO.setTotalAmountRefunded(totalAmountRefunded);
        statsDTO.setTotalRevenue(totalRevenue);

        return statsDTO;
    }

    @Override
    @Transactional
    public void cancelBooking(int bookingId) throws ResourceNotFoundException {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (BookingStatus.CANCELLED == booking.getStatus()) {
            throw new BadRequestException("Booking already cancelled");
        }

        User passenger = booking.getPassenger();
        User operator = booking.getOperator();

        double refundAmount = booking.getTotalFare();

        if (operator.getWallet() < refundAmount) {
            throw new BadRequestException("Operator has insufficient balance");
        }

        operator.setWallet(operator.getWallet() - refundAmount);
        passenger.setWallet(passenger.getWallet() + refundAmount);
        booking.setStatus(BookingStatus.CANCELLED);

        Refund refund = new Refund();

        refund.setBooking(booking);
        refund.setPassenger(passenger);
        refund.setOperator(operator);
        refund.setAmount(refundAmount);
        refund.setStatus(RefundStatus.APPROVED);

        refundRepository.save(refund);
        bookingRepository.save(booking);
    }
}