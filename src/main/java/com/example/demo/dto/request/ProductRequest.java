package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record ProductRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Category is required") String category,
        String description,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive") Double price,
        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative") Integer stock,
        @NotEmpty(message = "At least one image URL is required")
        @Size(min = 1, max = 5, message = "Image URLs must be between 1 and 5")
        List<@Pattern(regexp = "^(https?://.*\\.(?:png|jpg|jpeg|gif))$", message = "Invalid image URL") String> imageUrls
) {}