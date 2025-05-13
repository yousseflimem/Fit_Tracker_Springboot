package com.example.demo.repository;

import com.example.demo.model.entity.GymClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<GymClass, Long> {
    Page<GymClass> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String titleKeyword, String categoryKeyword, Pageable pageable);
    Page<GymClass> findByCoachId(Long coachId, Pageable pageable); // New method
}