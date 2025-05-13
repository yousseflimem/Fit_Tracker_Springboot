package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record WorkoutRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Category is required") String category,
        String description,
        @Positive(message = "Duration must be positive") Integer durationInMinutes,
        @NotNull(message = "Coach ID is required") Long coachId,
        @NotEmpty(message = "At least one image URL is required")
        List<String> imageUrls
) { }