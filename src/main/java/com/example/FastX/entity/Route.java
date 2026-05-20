package com.example.FastX.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routeId;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int durationInMinutes;

    @JsonBackReference(value = "route-bus")
    @OneToOne(mappedBy = "route")
    private Bus bus;
}