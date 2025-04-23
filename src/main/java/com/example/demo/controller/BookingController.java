package com.example.demo.controller;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public BookingResponse createBooking(@RequestBody BookingRequest request) {
        return bookingService.create(request);
    }

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<BookingResponse> getBookingsByUser(@PathVariable Long userId) {
        return bookingService.getByUserId(userId);
    }

    @PutMapping("/{id}")
    public BookingResponse updateBooking(@PathVariable Long id, @RequestBody BookingRequest request) {
        return bookingService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
    }
}
