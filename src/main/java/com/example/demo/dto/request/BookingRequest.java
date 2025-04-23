package com.example.demo.dto.request;

import com.example.demo.model.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    private Long userId;
    private Long classId;
    private LocalDateTime bookingDate;
    private BookingStatus status;
}
