package com.example.demo.dto.response;

import com.example.demo.model.entity.Review;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        Long userId,
        String username,
        Long workoutId
) {
    public ReviewResponse(Review review) {
        this(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getWorkout().getId()
        );
    }
}