package com.example.FastX.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booked_seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookedSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookedSeatId;
    @Positive
    private int seatNo;

    @JsonBackReference(value = "booking-seat")
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}