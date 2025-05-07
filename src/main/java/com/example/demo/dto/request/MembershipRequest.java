package com.example.demo.dto.request;

import com.example.demo.model.enums.MembershipType;

public record MembershipRequest(
        String name,
        String description,
        MembershipType type,
        Double price,
        Integer duration,
        Boolean active
) { }
