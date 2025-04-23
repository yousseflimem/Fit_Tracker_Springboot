package com.example.demo.dto.request;

public record ProductRequest(
        String name,
        String category,
        String description,
        Double price,
        Integer stock,  // Must be present here
        String imageUrl
) { }