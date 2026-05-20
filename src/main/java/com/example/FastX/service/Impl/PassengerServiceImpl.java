package com.example.FastX.service.Impl;

import com.example.FastX.constants.AuthProvider;
import com.example.FastX.constants.BookingStatus;
import com.example.FastX.constants.RefundStatus;
import com.example.FastX.dto.*;
import com.example.FastX.entity.*;
import com.example.FastX.exception.BadRequestException;
import com.example.FastX.exception.ResourceNotFoundException;
import com.example.FastX.repository.*;
import com.example.FastX.service.EmailService;
import com.example.FastX.service.PassengerService;
import com.example.FastX.util.Mapper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final BookingRepository bookingRepository;
    private final BookedSeatRepository seatRepository;
    private final RefundRepository refundRepository;
    private final EmailService emailService;

    private User getCurrentPassenger() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndActiveTrue(email);
    }

    @Override
    public PassengerDTO getProfile() {
        return Mapper.toPassengerDTO(getCurrentPassenger());
    }

    @Override
    public PassengerDTO updateProfile(UserUpdateDTO dto) {
        User passenger = getCurrentPassenger();

        passenger.setName(dto.getName());
        passenger.setPhone(dto.getPhone());
        passenger.setGender(dto.getGender());
        passenger.setAddress(dto.getAddress());

        return Mapper.toPassengerDTO(userRepository.save(passenger));
    }

    @Override
    public List<RouteSearchResponseDTO> searchRoutes(String origin, String destination, LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Route> routes = routeRepository
                .findByOriginAndDestinationAndDepartureTimeBetween(
                        origin,
                        destination,
                        startOfDay,
                        endOfDay
                );

        return routes.stream().map(route -> {

            Bus bus = route.getBus();
            if (bus == null) return null;

            List<Integer> booked = seatRepository.findBookedSeatsByBusId(bus.getBusId());
            int availableSeats = bus.getNoOfSeats() - booked.size();

            return new RouteSearchResponseDTO(
                    bus.getBusId(),
                    bus.getName(),
                    bus.getBusNumber(),
                    bus.getBusCategory(),
                    bus.isAc(),
                    bus.isWaterBottle(),
                    bus.isBlanket(),
                    bus.isTv(),
                    bus.isChargingPoint(),
                    bus.isSleeper(),
                    bus.getNoOfSeats(),
                    route.getOrigin(),
                    route.getDestination(),
                    route.getDepartureTime(),
                    route.getDepartureTime().plusMinutes(route.getDurationInMinutes()),
                    route.getDurationInMinutes(),
                    bus.getFare(),
                    availableSeats
            );

        }).filter(Objects::nonNull).toList();
    }

    @Override
    public List<RouteSearchResponseDTO> getAllRoutes() {

        List<Route> routes = routeRepository.findAll();

        return routes.stream().map(route -> {
            Bus bus = route.getBus();
            if (bus == null) return null;
            List<Integer> booked = seatRepository.findBookedSeatsByBusId(bus.getBusId());
            int availableSeats = bus.getNoOfSeats() - booked.size();
            return new RouteSearchResponseDTO(
                    bus.getBusId(),
                    bus.getName(),
                    bus.getBusNumber(),
                    bus.getBusCategory(),
                    bus.isAc(),
                    bus.isWaterBottle(),
                    bus.isBlanket(),
                    bus.isTv(),
                    bus.isChargingPoint(),
                    bus.isSleeper(),
                    bus.getNoOfSeats(),
                    route.getOrigin(),
                    route.getDestination(),
                    route.getDepartureTime(),
                    route.getDepartureTime()
                            .plusMinutes(route.getDurationInMinutes()),
                    route.getDurationInMinutes(),
                    bus.getFare(),
                    availableSeats
            );

        }).filter(Objects::nonNull).toList();
    }

    @Override
    public List<Integer> getAvailableSeats(int busId) throws ResourceNotFoundException {

        Bus bus = busRepository.findById(busId).orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        Set<Integer> booked = new HashSet<>(seatRepository.findBookedSeatsByBusId(busId));
        List<Integer> available = new ArrayList<>();

        for (int i = 1; i <= bus.getNoOfSeats(); i++) {
            if (!booked.contains(i)) {
                available.add(i);
            }
        }

        return available;
    }

    @Transactional
    @Override
    public BookingsResponseDTO bookTicket(BookingRequestDTO dto) throws ResourceNotFoundException, MessagingException {

        User passenger = getCurrentPassenger();

        Bus bus = busRepository.findById(dto.getBusId()).orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        User operator = bus.getOperator();

        double totalFare = dto.getTotalFare();
        double busFarePerSeat = bus.getFare();
        double expectedFare = busFarePerSeat * dto.getSeatNumbers().size();

        if (Math.abs(totalFare - expectedFare) > 0.01) {
            throw new BadRequestException("Invalid fare calculation");
        }

        if (passenger.getWallet() < totalFare) {
            throw new BadRequestException("Insufficient balance");
        }

        passenger.setWallet(passenger.getWallet() - totalFare);
        operator.setWallet(operator.getWallet() + totalFare);

        Set<Integer> bookedSeats = new HashSet<>(seatRepository.findBookedSeatsByBusId(bus.getBusId()));

        for (int seat : dto.getSeatNumbers()) {
            if (bookedSeats.contains(seat)) {
                throw new BadRequestException("Seat already booked: " + seat);
            }
        }

        Booking booking = new Booking();
        booking.setPassenger(passenger);
        booking.setBus(bus);
        booking.setOperator(operator);
        booking.setTotalNoOfSeats(dto.getSeatNumbers().size());
        booking.setTotalFare(totalFare);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);
        List<BookedSeat> bookedSeatList = new ArrayList<>();

        for (Integer seatNo : dto.getSeatNumbers()) {
            BookedSeat seat = new BookedSeat();
            seat.setSeatNo(seatNo);
            seat.setBooking(savedBooking);
            bookedSeatList.add(seat);
            seatRepository.save(seat);
        }

        savedBooking.setBookedSeats(bookedSeatList);

        emailService.sendBookingConfirmation(
                passenger,
                booking
        );
        return Mapper.toBookingResponseDTO(savedBooking);
    }

    @Override
    public List<BookingsResponseDTO> getAllBookings() {
        return bookingRepository
                .findByPassenger_IdOrderByBookingTimeDesc(getCurrentPassenger().getId())
                .stream()
                .map(Mapper::toBookingResponseDTO)
                .toList();
    }

    @Override
    public List<BookingsResponseDTO> getActiveBookings() {
        return bookingRepository
                .findByPassenger_IdAndStatusInOrderByBookingTimeDesc(getCurrentPassenger().getId(), List.of(BookingStatus.CONFIRMED, BookingStatus.PROCESSING))
                .stream()
                .map(Mapper::toBookingResponseDTO)
                .toList();
    }

    @Override
    public List<BookingsResponseDTO> getPastBookings() {
        return bookingRepository
                .findByPassenger_IdAndStatusInOrderByBookingTimeDesc(getCurrentPassenger().getId(), List.of(BookingStatus.CANCELLED, BookingStatus.COMPLETED))
                .stream()
                .map(Mapper::toBookingResponseDTO)
                .toList();

    }

    @Override
    public BookingsResponseDTO getBookingById(int id) throws ResourceNotFoundException {
        return Mapper.toBookingResponseDTO(bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking not found")));
    }

    @Override
    public void requestRefund(int bookingId) throws ResourceNotFoundException {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (BookingStatus.CONFIRMED != booking.getStatus()) {
            throw new BadRequestException("Only confirmed bookings can request refund");
        }

        if (booking.getRefund() != null) {
            throw new BadRequestException("Refund already requested for this booking");
        }

        booking.setStatus(BookingStatus.PROCESSING);

        Refund refund = new Refund();
        refund.setBooking(booking);
        refund.setPassenger(getCurrentPassenger());
        refund.setOperator(booking.getOperator());
        refund.setAmount(booking.getTotalFare());
        refund.setStatus(RefundStatus.PENDING);

        refundRepository.save(refund);
        bookingRepository.save(booking);
    }

    @Override
    public double addMoney(double amount) {

        if (amount <= 0) {
            throw new BadRequestException("Amount must be positive");
        }

        User passenger = getCurrentPassenger();
        passenger.setWallet(passenger.getWallet() + amount);

        return userRepository.save(passenger).getWallet();
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {

        User passenger = getCurrentPassenger();

        if (newPassword == null || newPassword.isEmpty()) {
            throw new BadRequestException("Password must not be empty");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        if ((AuthProvider.GOOGLE == passenger.getProvider()) && !passenger.isPasswordChanged()) {
            passenger.setPassword(encoder.encode(newPassword));
            passenger.setPasswordChanged(true);
            userRepository.save(passenger);
            return;
        }

        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new BadRequestException("Old password is required");
        }

        if (!encoder.matches(oldPassword, passenger.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        passenger.setPassword(encoder.encode(newPassword));
        userRepository.save(passenger);
    }
}