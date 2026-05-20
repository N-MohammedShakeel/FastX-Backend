package com.example.FastX.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int busId;
    private String busNumber;
    private String name;
    private String busCategory;
    private int noOfSeats;
    private double fare;
    private boolean ac;
    private boolean waterBottle;
    private boolean blanket;
    private boolean tv;
    private boolean chargingPoint;
    private boolean sleeper;

    @JsonBackReference(value = "operator-bus")
    @ManyToOne
    @JoinColumn(name = "operator_id")
    private User operator;

    @JsonManagedReference(value = "route-bus")
    @OneToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @JsonManagedReference(value = "bus-booking")
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}