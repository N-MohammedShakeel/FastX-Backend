package com.example.FastX.scheduler;

import com.example.FastX.constants.BookingStatus;
import com.example.FastX.entity.Booking;
import com.example.FastX.entity.Bus;
import com.example.FastX.entity.Route;
import com.example.FastX.repository.BookingRepository;
import com.example.FastX.repository.BusRepository;
import com.example.FastX.repository.RouteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class BookingStatusScheduler {

    private final BookingRepository bookingRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;

    @Transactional
    @Scheduled(fixedRate = 120000)
    public void updateCompletedBookings() {

        List<Booking> confirmedBookings =
                bookingRepository.findByStatus(
                        BookingStatus.CONFIRMED
                );

        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : confirmedBookings) {

            Bus bus = booking.getBus();
            Route route = bus.getRoute();

            if (route == null) continue;

            LocalDateTime arrivalTime =
                    route.getDepartureTime()
                            .plusMinutes(
                                    route.getDurationInMinutes()
                            );

            if (now.isAfter(arrivalTime)) {

                booking.setStatus(
                        BookingStatus.COMPLETED
                );

                bookingRepository.save(booking);

                bus.setRoute(null);
                busRepository.save(bus);
                routeRepository.delete(route);
            }
        }
    }
}