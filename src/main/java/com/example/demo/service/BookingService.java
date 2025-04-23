package com.example.demo.service;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse create(BookingRequest request);
    List<BookingResponse> getAll();
    List<BookingResponse> getByUserId(Long userId);
    BookingResponse update(Long id, BookingRequest request);
    void delete(Long id);
}
