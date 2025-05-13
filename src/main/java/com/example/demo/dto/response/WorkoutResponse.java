package com.example.demo.dto.response;

import com.example.demo.model.entity.Image;
import com.example.demo.model.entity.Workout;
import java.util.List;

public record WorkoutResponse(
        Long id,
        String name,
        String category,
        String description,
        Integer durationInMinutes,
        Integer viewCount,
        Long coachId,
        String coachName,
        String coachProfileImageUrl,
        List<String> imageUrls
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
                workout.getCoach().getUsername(),
                workout.getCoach().getProfileImageUrl(),
                workout.getImages().stream()
                        .map(Image::getUrl)
                        .toList()
        );
    }
}