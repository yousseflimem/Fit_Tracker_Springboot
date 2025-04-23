package com.example.demo.dto.response;

import com.example.demo.model.entity.Workout;

public record WorkoutResponse(
        Long id,
        String name,
        String category,
        String description,
        Integer durationInMinutes,
        Integer viewCount,
        Long coachId,
        String coachName
) {
    public WorkoutResponse(Workout workout) {
        this(
                workout.getId(),
                workout.getName(),
                workout.getCategory(),
                workout.getDescription(),
                workout.getDuration(),
                workout.getViewCount(),
                workout.getCoach().getId(),
                workout.getCoach().getUsername()
        );
    }
}