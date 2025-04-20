package com.example.demo.repository;

import com.example.demo.entity.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {
}
