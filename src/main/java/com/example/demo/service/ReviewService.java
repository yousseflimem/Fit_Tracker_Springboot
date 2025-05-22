package com.example.demo.service;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface ReviewService {
    Page<ReviewResponse> getReviewsByWorkout(Long workoutId, int page, int size);
    Page<ReviewResponse> getReviewsByUserId(Long userId, int page, int size);
    ReviewResponse createReview(ReviewRequest request, Long userId, Authentication authentication);
    void deleteReview(Long id, Long userId, Authentication authentication);
    Page<ReviewResponse> getAllReviews(int page, int size);
}