package com.example.FastX.repository;

import com.example.FastX.constants.RefundStatus;
import com.example.FastX.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {
    List<Refund> findByOperator_Id(int operatorId);
    boolean existsByOperator_IdAndStatus(int id, RefundStatus status);
    boolean existsByPassenger_IdAndStatus(int id, RefundStatus status);
}