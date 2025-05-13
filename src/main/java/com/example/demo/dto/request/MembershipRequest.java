package com.example.demo.dto.request;

import com.example.demo.model.enums.MembershipType;
import jakarta.validation.constraints.*;

public record MembershipRequest(
        @NotBlank(message = "Name is required") String name,
        String description,
        @NotNull(message = "Type is required") MembershipType type,
        @NotNull(message = "Price is required") @Positive(message = "Price must be positive") Double price,
        @NotNull(message = "Duration is required") @Positive(message = "Duration must be positive") Integer duration,
        Boolean active
) { }