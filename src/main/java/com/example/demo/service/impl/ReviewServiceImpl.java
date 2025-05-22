package com.example.demo.service.impl;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Review;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.Workout;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkoutRepository;
import com.example.demo.service.ReviewService;
import com.example.demo.service.SecurityService;
import com.example.demo.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            WorkoutRepository workoutRepository,
            SecurityService securityService
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByWorkout(Long workoutId, int page, int size) {
        if (!workoutRepository.existsById(workoutId)) {
            throw new ResourceNotFoundException("Workout not found with id: " + workoutId);
        }
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be non-negative and size must be positive");
        }
        // Use Spring's PageRequest directly
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository
                .findByWorkoutId(workoutId, pageRequest)
                .map(ReviewResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getAllReviews(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be non-negative and size must be positive");
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findAll(pageRequest).map(ReviewResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByUserId(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be non-negative and size must be positive");
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findByUserId(userId, pageRequest)
                .map(ReviewResponse::new);
    }

    @Override
    @Transactional
    public ReviewResponse createReview(ReviewRequest request, Long userId, Authentication authentication) {
        logger.debug("Creating review for user id: {}, workout id: {}", userId, request.workoutId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Workout workout = workoutRepository.findById(request.workoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + request.workoutId()));

        Review review = new Review();
        review.setUser(user);
        review.setWorkout(workout);
        review.setRating(request.rating());
        review.setComment(request.comment());

        Review savedReview = reviewRepository.save(review);
        logger.info("Created review with id: {} for workout id: {}", savedReview.getId(), workout.getId());
        return new ReviewResponse(savedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id, Long userId, Authentication authentication) {
        logger.debug("Deleting review id: {} by user id: {}", id, userId);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        if (!review.getUser().getId().equals(userId) && !authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            logger.warn("User id: {} attempted to delete review id: {} without permission", userId, id);
            throw new SecurityException("You do not have permission to delete this review");
        }
        reviewRepository.deleteById(id);
        logger.info("Deleted review with id: {}", id);
    }
}