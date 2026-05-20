package com.example.FastX.repository;

import com.example.FastX.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {

    List<Route> findByOriginAndDestinationAndDepartureTimeBetween(String origin, String destination, LocalDateTime startOfDay, LocalDateTime endOfDay);
}