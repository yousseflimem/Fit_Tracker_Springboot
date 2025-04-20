package com.example.demo.repository;

import com.example.demo.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findWorkoutById(Long id); // Custom method with a different name
}
