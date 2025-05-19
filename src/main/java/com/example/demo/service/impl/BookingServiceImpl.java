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
import com.example.demo.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;

    @Autowired
    public BookingServiceImpl(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ClassRepository classRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.classRepository = classRepository;
    }

    @Override
    @Transactional
    public BookingResponse create(BookingRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));
        GymClass gymClass = classRepository.findById(request.classId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + request.classId()));

        // Validate booking date
        ZonedDateTime bookingDate = request.bookingDate() != null
                ? request.bookingDate()
                : ZonedDateTime.now(ZoneId.of("UTC"));
        if (bookingDate.isAfter(gymClass.getStartTime())) {
            throw new IllegalStateException("Cannot book a class that has already started.");
        }

        // Check class capacity
        long currentBookings = bookingRepository.countByGymClassIdAndStatusNot(gymClass.getId(), BookingStatus.CANCELLED);
        if (gymClass.getCapacity() != null && currentBookings >= gymClass.getCapacity()) {
            throw new IllegalStateException("Class is fully booked: " + gymClass.getTitle());
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setGymClass(gymClass);
        booking.setBookingDate(bookingDate);
        booking.setStatus(BookingStatus.PENDING);

        return new BookingResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveBookingsCount(Long classId) {
        if (!classRepository.existsById(classId)) {
            throw new ResourceNotFoundException("Class not found with id: " + classId);
        }
        return bookingRepository.countByGymClassIdAndStatusNot(classId, BookingStatus.CANCELLED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> getAll(int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        return bookingRepository.findAll(pageRequest)
                .map(BookingResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(BookingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse update(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));
        GymClass gymClass = classRepository.findById(request.classId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + request.classId()));

        // Check class capacity if changing class
        if (!booking.getGymClass().getId().equals(gymClass.getId())) {
            long currentBookings = bookingRepository.countByGymClassIdAndStatusNot(gymClass.getId(), BookingStatus.CANCELLED);
            if (gymClass.getCapacity() != null && currentBookings >= gymClass.getCapacity()) {
                throw new IllegalStateException("Class is fully booked: " + gymClass.getTitle());
            }
        }

        booking.setGymClass(gymClass);
        booking.setUser(user);
        booking.setStatus(request.status() != null ? request.status() : BookingStatus.PENDING);
        booking.setBookingDate(request.bookingDate() != null
                ? request.bookingDate()
                : ZonedDateTime.now(ZoneId.of("UTC")));

        return new BookingResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }
}