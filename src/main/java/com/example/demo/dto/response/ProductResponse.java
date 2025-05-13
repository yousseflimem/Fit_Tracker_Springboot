package com.example.demo.dto.response;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        Double price,
        String description,
        Integer stock,
        Integer totalSold,
        String category,
        List<String> imageUrls  // Changed to support multiple images
) { }