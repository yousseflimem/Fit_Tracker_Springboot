package com.example.demo.dto.response;

import java.time.LocalDateTime;
import com.example.demo.model.entity.GymClass;


public record ClassResponse(
        Long id,
        String title,
        String category,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer duration,
        Integer capacity,
        Long coachId,
        String coachName,
        String imageUrl
) {
    public ClassResponse(GymClass gymClass) {
        this(
                gymClass.getId(),
                gymClass.getTitle(),
                gymClass.getCategory(),
                gymClass.getDescription(),
                gymClass.getStartTime(),
                gymClass.getEndTime(),
                gymClass.getDuration(),
                gymClass.getCapacity(),
                gymClass.getCoach().getId(),
                gymClass.getCoach().getUsername(),
                gymClass.getImageUrl()
        );
    }
}