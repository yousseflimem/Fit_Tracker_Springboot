package com.example.demo.dto.request;

import java.time.LocalDateTime;

public record ClassRequest(
        String title,
        String category,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer capacity,
        Long coachId,  // Required to link to Coach
        String imageUrl
) { }