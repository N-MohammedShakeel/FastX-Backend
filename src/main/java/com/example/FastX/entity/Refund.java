package com.example.FastX.entity;

import com.example.FastX.constants.RefundStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refunds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int refundId;
    private double amount;
    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    @JsonBackReference(value = "booking-refund")
    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @JsonBackReference(value = "operator-refund")
    @ManyToOne
    @JoinColumn(name = "operator_id")
    private User operator;

    @JsonBackReference(value = "passenger-refund")
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private User passenger;
}