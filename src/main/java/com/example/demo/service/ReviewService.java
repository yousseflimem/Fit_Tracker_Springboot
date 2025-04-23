package com.example.demo.service;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;

public interface ReviewService {
    Page<ReviewResponse> getReviewsByWorkout(Long workoutId, int page, int size);
    ReviewResponse createReview(ReviewRequest request);
    void deleteReview(Long id);
}
