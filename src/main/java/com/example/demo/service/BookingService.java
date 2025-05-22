package com.example.demo.service;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {
    BookingResponse create(BookingRequest request);
    Page<BookingResponse> getAll(int page, int size);
    List<BookingResponse> getByUserId(Long userId);
    BookingResponse update(Long id, BookingRequest request);
    void delete(Long id);
    long getActiveBookingsCount(Long classId); // New method
    List<BookingResponse> getByClassId(Long classId);
}