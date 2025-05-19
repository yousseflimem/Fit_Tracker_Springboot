package com.example.demo.dto.request;

import com.example.demo.model.enums.BookingStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.ZonedDateTime;

public record BookingRequest(
        @NotNull @Positive Long userId,
        @NotNull @Positive Long classId,
        @FutureOrPresent ZonedDateTime bookingDate,
        BookingStatus status
) { }