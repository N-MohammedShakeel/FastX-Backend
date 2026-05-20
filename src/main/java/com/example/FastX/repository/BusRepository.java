package com.example.FastX.repository;

import com.example.FastX.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {
    List<Bus> findByOperator_Id(int operatorId);
    Bus findByRoute_RouteId(int routeId);
}