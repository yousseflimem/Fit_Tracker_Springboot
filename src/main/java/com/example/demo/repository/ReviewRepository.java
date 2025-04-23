package com.example.demo.repository;

import com.example.demo.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Spring Data will translate this into "where workout.id = :workoutId"
    Page<Review> findByWorkout_Id(Long workoutId, Pageable pageable);
}
