package com.example.demo.dto.response;

public record ProductResponse(
        Long id,
        String name,
        Double price,
        String description,
        Integer stock,
        Integer totalSold,  // Calculated field
        String category,
        String imageUrl
) { }