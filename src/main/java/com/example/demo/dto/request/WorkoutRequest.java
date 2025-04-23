package com.example.demo.dto.request;

public record WorkoutRequest(
        String name,
        String category,
        String description,
        Integer durationInMinutes,
        Long coachId
) { }