package com.example.demo.dto.response;

import com.example.demo.model.entity.GymClass;
import com.example.demo.model.entity.Image;
import com.example.demo.model.entity.Workout;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ClassResponse(
        Long id,
        String title,
        String category,
        String description,
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        Integer duration,
        Integer capacity,
        Long coachId,
        String coachName,
        List<String> imageUrls,
        List<Long> workoutIds
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
                gymClass.getImages() != null
                        ? gymClass.getImages().stream().map(Image::getUrl).collect(Collectors.toList())
                        : List.of(),
                gymClass.getWorkouts() != null
                        ? gymClass.getWorkouts().stream().map(Workout::getId).collect(Collectors.toList())
                        : List.of()
        );
    }
}