package com.example.demo.controller;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired private ReviewService reviewService;

    @GetMapping("/workout/{workoutId}")
    public Page<ReviewResponse> getReviewsByWorkout(
            @PathVariable Long workoutId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size) {
        return reviewService.getReviewsByWorkout(workoutId, page, size);
    }


    @PostMapping
    public ReviewResponse createReview(@RequestBody ReviewRequest request) {
        return reviewService.createReview(request);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
