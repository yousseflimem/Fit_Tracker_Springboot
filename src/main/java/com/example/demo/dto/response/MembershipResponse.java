package com.example.demo.dto.response;

import com.example.demo.model.enums.MembershipType;
import java.time.LocalDateTime;

public record MembershipResponse(
        Long id,
        String name,
        String description,
        MembershipType type,
        Double price,
        Integer duration,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }