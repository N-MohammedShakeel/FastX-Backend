package com.example.FastX.repository;

import com.example.FastX.constants.BookingStatus;
import com.example.FastX.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByOperator_Id(int operatorId);
    boolean existsByPassenger_IdAndStatus(int id, BookingStatus booked);
    List<Booking> findByPassenger_IdOrderByBookingTimeDesc(int id);
    List<Booking> findByPassenger_IdAndStatusInOrderByBookingTimeDesc(int id, List<BookingStatus> status);
    List<Booking> findByStatus(BookingStatus status);
}