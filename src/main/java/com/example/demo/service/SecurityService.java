package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Booking;
import com.example.demo.model.entity.GymClass;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.Workout;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.WorkoutRepository;
import com.example.demo.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private final BookingRepository bookingRepository;
    private final ClassRepository classRepository;
    private final WorkoutRepository workoutRepository;
    private final OrderRepository orderRepository;

    public SecurityService(
            BookingRepository bookingRepository,
            ClassRepository classRepository,
            WorkoutRepository workoutRepository,
            OrderRepository orderRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.classRepository = classRepository;
        this.workoutRepository = workoutRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public boolean isUser(Long userId, Authentication authentication) {
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);
        logger.info("Checking isUser: requestUserId={}, authenticatedUserId={}", userId, authenticatedUserId);
        return authenticatedUserId != null && authenticatedUserId.equals(userId);
    }

    @Transactional(readOnly = true)
    public boolean isBookingOwner(Long bookingId, Authentication authentication) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);
        return authenticatedUserId != null && authenticatedUserId.equals(booking.getUser().getId());
    }

    @Transactional(readOnly = true)
    public boolean isCoachForClass(Long classId, Authentication authentication) {
        GymClass gymClass = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);
        return authenticatedUserId != null && authenticatedUserId.equals(gymClass.getCoach().getId());
    }

    @Transactional(readOnly = true)
    public boolean isCoachForWorkout(Long workoutId, Authentication authentication) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + workoutId));
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);
        return authenticatedUserId != null && authenticatedUserId.equals(workout.getCoach().getId());
    }

    @Transactional(readOnly = true)
    public boolean isOrderOwner(Long orderId, Authentication authentication) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        Long authenticatedUserId = getUserIdFromAuthentication(authentication);
        return authenticatedUserId != null && authenticatedUserId.equals(order.getUser().getId());
    }

    public Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Authentication is null or not authenticated");
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) principal).getId();
        }
        logger.warn("Principal is not an instance of UserDetailsImpl");
        return null;
    }
}