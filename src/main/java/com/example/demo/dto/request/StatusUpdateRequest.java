package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StatusUpdateRequest(
        @NotBlank(message = "Status is required")
        @Pattern(regexp = "PENDING|COMPLETED|CANCELLED", message = "Status must be PENDING, COMPLETED or CANCELLED")
        String status
) {}
