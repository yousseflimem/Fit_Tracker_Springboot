// src/main/java/com/example/demo/repository/ClassRepository.java
package com.example.demo.repository;

import com.example.demo.model.entity.GymClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<GymClass, Long> {

    Page<GymClass> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String keyword, String keyword1, PageRequest pageRequest);
}
