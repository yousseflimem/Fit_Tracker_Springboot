package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record ClassRequest(
        @NotBlank(message = "Title is required") String title,
        String category,
        String description,
        @NotNull(message = "Start time is required") @Future(message = "Start time must be in the future") LocalDateTime startTime,
        @NotNull(message = "End time is required") @Future(message = "End time must be in the future") LocalDateTime endTime,
        @PositiveOrZero(message = "Capacity must be non-negative") Integer capacity,
        @NotNull(message = "Coach ID is required") @Positive(message = "Coach ID must be positive") Long coachId,
        List<String> imageUrls,
        @NotEmpty(message = "At least one workout ID is required") List<Long> workoutIds
) { }