package com.example.FastX.repository;

import com.example.FastX.entity.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedSeatRepository extends JpaRepository<BookedSeat, Integer> {

    @Query("SELECT bs.seatNo FROM BookedSeat bs WHERE bs.booking.bus.busId = :busId")
    List<Integer> findBookedSeatsByBusId(int busId);
    void deleteByBooking_BookingId(int bookingId);
}