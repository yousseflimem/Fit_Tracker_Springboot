package com.example.demo.repository;

import com.example.demo.model.entity.Booking;
import com.example.demo.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    long countByGymClassIdAndStatusNot(Long gymClassId, BookingStatus status);
    List<Booking> findByGymClassId(Long gymClassId);
}