package com.example.demo.service.impl;

import com.example.demo.dto.request.BookingRequest;
import com.example.demo.dto.response.BookingResponse;
import com.example.demo.model.enums.BookingStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Booking;
import com.example.demo.model.entity.GymClass;
import com.example.demo.model.entity.User;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public BookingResponse create(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        GymClass gymClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setGymClass(gymClass);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);

        return new BookingResponse(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponse> getAll() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getByUserId(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(BookingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse update(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        GymClass gymClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        booking.setGymClass(gymClass);
        booking.setUser(user);
        booking.setStatus(request.getStatus());
        booking.setBookingDate(request.getBookingDate());

        return new BookingResponse(bookingRepository.save(booking));
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
