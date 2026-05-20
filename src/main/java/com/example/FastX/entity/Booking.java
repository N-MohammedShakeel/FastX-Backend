package com.example.FastX.entity;

import com.example.FastX.constants.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;
    private double totalFare;
    private int totalNoOfSeats;
    private LocalDateTime bookingTime;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @JsonBackReference(value = "passenger-booking")
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private User passenger;

    @JsonBackReference(value = "bus-booking")
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @JsonBackReference(value = "operator-booking")
    @ManyToOne
    @JoinColumn(name = "operator_id")
    private User operator;

    @JsonManagedReference(value = "booking-seat")
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookedSeat> bookedSeats;

    @JsonManagedReference(value = "booking-refund")
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Refund refund;
}