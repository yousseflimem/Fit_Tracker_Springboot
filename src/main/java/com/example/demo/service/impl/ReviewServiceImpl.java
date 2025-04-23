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
import com.example.demo.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WorkoutRepository workoutRepository;

    @Override
    public Page<ReviewResponse> getReviewsByWorkout(Long workoutId, int page, int size) {
        // sort by createdAt instead of the nonexistent 'name'
        PageRequest pr = PaginationUtil.createPageRequest(page, size, "createdAt");
        return reviewRepository.findByWorkout_Id(workoutId, pr)
                .map(ReviewResponse::new);
    }



    @Override
    public ReviewResponse createReview(ReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Workout workout = workoutRepository.findById(request.getWorkoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        Review r = new Review();
        r.setUser(user);
        r.setWorkout(workout);
        r.setRating(request.getRating());
        r.setComment(request.getComment());
        // createdAt is set by @PrePersist

        return new ReviewResponse(reviewRepository.save(r));
    }

    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found");
        }
        reviewRepository.deleteById(id);
    }
}
