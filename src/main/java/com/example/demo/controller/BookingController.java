package com.example.demo.controller;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('Coach')")
    public List<BookingResponse> getBookingsByClass(@PathVariable Long classId) {
        return bookingService.getByClassId(classId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request) {
        return bookingService.create(request);
    }

    @GetMapping("/count/{classId}")
    @PreAuthorize("permitAll()")
    public long getBookingsCountByClass(@PathVariable Long classId) {
        return bookingService.getActiveBookingsCount(classId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<BookingResponse> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return bookingService.getAll(page, size);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') ")
    public List<BookingResponse> getBookingsByUser(@PathVariable Long userId) {
        return bookingService.getByUserId(userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isBookingOwner(#id, authentication)")
    public BookingResponse updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        return bookingService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isBookingOwner(#id, authentication)")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
    }
}