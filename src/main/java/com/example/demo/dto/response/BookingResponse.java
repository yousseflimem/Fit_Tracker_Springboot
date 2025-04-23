package com.example.demo.dto.response;

import com.example.demo.model.enums.BookingStatus;
import com.example.demo.model.entity.Booking;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long classId;
    private LocalDateTime bookingDate;
    private BookingStatus status;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.userId = booking.getUser().getId();
        this.classId = booking.getGymClass().getId();
        this.bookingDate = booking.getBookingDate();
        this.status = booking.getStatus();
    }
}
