package com.example.demo.dto.request;

import com.example.demo.model.enums.MembershipType;

public record MembershipRequest(
        MembershipType type,
        Double price,
        Integer duration  // Renamed from `durationInDays`
) { }