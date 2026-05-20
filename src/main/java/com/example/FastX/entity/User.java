package com.example.FastX.entity;

import com.example.FastX.constants.AuthProvider;
import com.example.FastX.constants.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    private String gender;
    private String address;
    private double wallet = 0.0;
    private boolean passwordChanged = false;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private boolean active = true;

    @JsonManagedReference(value = "passenger-booking")
    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @JsonManagedReference(value = "operator-bus")
    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL)
    private List<Bus> buses;

    @JsonManagedReference(value = "passenger-refund")
    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
    private List<Refund> refunds;
}

