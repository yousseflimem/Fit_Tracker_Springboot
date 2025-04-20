package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Custom query methods can be defined here if needed
    // For example, find bookings by user ID or date range
    List<Booking> findByUserId(Long userId);
}
