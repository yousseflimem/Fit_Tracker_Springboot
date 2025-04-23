// src/main/java/com/example/demo/repository/BookingRepository.java
package com.example.demo.repository;

import com.example.demo.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByUserId(Long userId, Pageable pageable);

    List<Booking> findByUserId(Long userId);
}
