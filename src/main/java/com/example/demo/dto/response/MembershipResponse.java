package com.example.demo.dto.response;

import com.example.demo.model.enums.MembershipType;

public record MembershipResponse(
        Long id,
        MembershipType type,
        Double price,
        Integer duration  // Renamed from `durationInDays`
) { }