package com.example.FastX.util;

import com.example.FastX.constants.AuthProvider;
import com.example.FastX.constants.BookingStatus;
import com.example.FastX.dto.PassengerDTO;
import com.example.FastX.entity.User;
import com.example.FastX.dto.*;
import com.example.FastX.entity.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Mapper {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public static PassengerDTO toPassengerDTO(User user) {
        if (user == null) return null;
        PassengerDTO dto = new PassengerDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setWallet(user.getWallet());
        dto.setProvider(user.getProvider());
        dto.setPasswordChanged(user.isPasswordChanged());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());

        if (user.getBookings() != null) {
            dto.setBookings(user.getBookings().stream()
                    .map(Mapper::toBookingResponseDTO).collect(Collectors.toList()));
        }
        if (user.getRefunds() != null) {
            dto.setRefunds(user.getRefunds().stream()
                    .map(Mapper::toRefundRequestDTO).collect(Collectors.toList()));
        }
        return dto;
    }

    public static OperatorDTO toOperatorDTO(User user) {
        if (user == null) return null;
        OperatorDTO dto = new OperatorDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setWallet(user.getWallet());
        dto.setProvider(user.getProvider());
        dto.setPasswordChanged(user.isPasswordChanged());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());

        if (user.getBuses() != null) {
            dto.setBuses(user.getBuses().stream().map(Mapper::toBusResponseDTO).toList());
        }
        if (user.getRefunds() != null){
            dto.setRefunds(user.getRefunds().stream().map(Mapper::toRefundResponseDTO).toList());
        }
        return dto;
    }

    public static BookingsResponseDTO toBookingResponseDTO(Booking booking) {
        if (booking == null) return null;

        BookingsResponseDTO dto = new BookingsResponseDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setTotalFare(booking.getTotalFare());
        dto.setTotalNoOfSeats(booking.getTotalNoOfSeats());
        dto.setBookingTime(booking.getBookingTime());
        dto.setStatus(booking.getStatus());

        if (booking.getBus() != null) {
            dto.setBusName(booking.getBus().getName());
            dto.setBusNumber(booking.getBus().getBusNumber());
            
            if (booking.getBus().getRoute() != null) {
                Route route = booking.getBus().getRoute();
                dto.setOrigin(route.getOrigin());
                dto.setDestination(route.getDestination());
                dto.setDepartureTime(route.getDepartureTime());
                dto.setArrivalTime(route.getDepartureTime().plusMinutes(route.getDurationInMinutes()));
            }
        }
        if (booking.getBookedSeats() != null && !booking.getBookedSeats().isEmpty()) {
            dto.setSeatNumbers(booking.getBookedSeats().stream()
                    .filter(Objects::nonNull)
                    .map(BookedSeat::getSeatNo)
                    .sorted()
                    .toList());
        } else {
            dto.setSeatNumbers(new ArrayList<>());
        }
        if (booking.getPassenger() != null){
            dto.setPassengerName(booking.getPassenger().getName());
        }
        return dto;
    }

    public static BusRequestDTO toBusRequestDTO(Bus bus) {
        if (bus == null) return null;
        BusRequestDTO dto = new BusRequestDTO();
        dto.setBusNumber(bus.getBusNumber());
        dto.setName(bus.getName());
        dto.setNoOfSeats(bus.getNoOfSeats());
        dto.setFare(bus.getFare());
        dto.setBusCategory(bus.getBusCategory());
        dto.setAc(bus.isAc());
        dto.setWaterBottle(bus.isWaterBottle());
        dto.setBlanket(bus.isBlanket());
        dto.setTv(bus.isTv());
        dto.setChargingPoint(bus.isChargingPoint());
        dto.setSleeper(bus.isSleeper());
        if (bus.getRoute() != null) {
            dto.setRouteId(bus.getRoute().getRouteId());
        }
        return dto;
    }

    public static RefundRequestDTO toRefundRequestDTO(Refund refund) {
        if (refund == null) return null;
        RefundRequestDTO dto = new RefundRequestDTO();
        dto.setStatus(refund.getStatus());
        if (refund.getBooking() != null) {
            dto.setBookingId(refund.getBooking().getBookingId());
        }
        return dto;
    }

    public static RouteRequestDTO toRouteRequestDTO(Route route) {
        if (route == null) return null;
        RouteRequestDTO dto = new RouteRequestDTO();
        dto.setOrigin(route.getOrigin());
        dto.setDestination(route.getDestination());
        dto.setDepartureTime(route.getDepartureTime());
        dto.setDurationInMinutes(route.getDurationInMinutes());
        return dto;
    }

    public User toUser(UserRegisterDTO dto){
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        return user;
    }

    public static BusResponseDTO toBusResponseDTO(Bus bus) {

        if (bus == null) return null;

        BusResponseDTO dto = new BusResponseDTO();

        dto.setBusId(bus.getBusId());
        dto.setBusNumber(bus.getBusNumber());
        dto.setName(bus.getName());
        dto.setBusCategory(bus.getBusCategory());
        dto.setNoOfSeats(bus.getNoOfSeats());
        dto.setFare(bus.getFare());
        dto.setWaterBottle(bus.isWaterBottle());
        dto.setBlanket(bus.isBlanket());
        dto.setChargingPoint(bus.isChargingPoint());
        dto.setTv(bus.isTv());
        dto.setAc(bus.isAc());
        dto.setSleeper(bus.isSleeper());



        if (bus.getRoute() != null) {

            Route route = bus.getRoute();
            dto.setOrigin(route.getOrigin());
            dto.setDestination(route.getDestination());
            dto.setDepartureTime(route.getDepartureTime());
            dto.setRouteId(route.getRouteId());
        }

        int bookedSeats = 0;

        if (bus.getBookings() != null) {

            bookedSeats = bus.getBookings().stream()
                    .filter(b -> BookingStatus.CONFIRMED.equals(b.getStatus()))
                    .mapToInt(Booking::getTotalNoOfSeats)
                    .sum();
        }

        dto.setSeatsLeft(bus.getNoOfSeats() - bookedSeats);

        return dto;
    }

    public static RouteResponseDTO toRouteResponseDTO(Route route) {

        if (route == null) return null;

        RouteResponseDTO dto = new RouteResponseDTO();

        dto.setRouteId(route.getRouteId());
        dto.setOrigin(route.getOrigin());
        dto.setDestination(route.getDestination());
        dto.setDepartureTime(route.getDepartureTime());
        dto.setDurationInMinutes(route.getDurationInMinutes());

        Bus bus = route.getBus();
        dto.setAssigned(bus != null);

        if (bus != null) {
            dto.setBusName(bus.getName());
            dto.setBusNumber(bus.getBusNumber());
            dto.setOperatorName(bus.getOperator().getName());
        }

        return dto;
    }

    public static RefundResponseDTO toRefundResponseDTO(Refund refund) {

        if (refund == null) return null;

        RefundResponseDTO dto = new RefundResponseDTO();

        dto.setRefundId(refund.getRefundId());
        dto.setAmount(refund.getAmount());
        dto.setStatus(refund.getStatus());

        if (refund.getBooking() != null) {

            Booking booking = refund.getBooking();

            dto.setBookingId(booking.getBookingId());
            dto.setBookingTime(booking.getBookingTime());

            if (booking.getPassenger() != null) {
                dto.setPassengerName(booking.getPassenger().getName());
            }
            if (booking.getBus() != null) {
                dto.setBusNumber(booking.getBus().getBusNumber());
            }
        }

        return dto;
    }
}