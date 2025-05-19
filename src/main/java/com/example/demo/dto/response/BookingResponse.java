package com.example.demo.dto.response;

import com.example.demo.model.entity.Booking;
import com.example.demo.model.enums.BookingStatus;

import java.time.ZonedDateTime;

public record BookingResponse(
        Long id,
        Long userId,
        Long classId,
        String classTitle,
        ZonedDateTime bookingDate,
        BookingStatus status
) {
    public BookingResponse(Booking booking) {
        this(
                booking.getId(),
                booking.getUser().getId(),
                booking.getGymClass().getId(),
                booking.getGymClass().getTitle(),
                booking.getBookingDate(),
                booking.getStatus()
        );
    }
}