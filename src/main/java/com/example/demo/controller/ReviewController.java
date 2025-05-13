package com.example.demo.controller;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.service.ReviewService;
import com.example.demo.service.SecurityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityService securityService;

    public ReviewController(ReviewService reviewService, SecurityService securityService) {
        this.reviewService = reviewService;
        this.securityService = securityService;
    }

    @GetMapping("/workout/{workoutId}")
    public Page<ReviewResponse> getReviewsByWorkout(
            @PathVariable Long workoutId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be non-negative") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be positive") int size
    ) {
        return reviewService.getReviewsByWorkout(workoutId, page, size);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public Page<ReviewResponse> getUserReviews(
            @RequestParam(required = false) Long userId,
            @RequestParam @Min(value = 0, message = "Page must be non-negative") int page,
            @RequestParam @Min(value = 1, message = "Size must be positive") int size,
            Authentication authentication
    ) {
        Long authenticatedUserId = securityService.getUserIdFromAuthentication(authentication);
        if (authenticatedUserId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }

        if (userId != null) {
            if (!authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                throw new SecurityException("Only admins can view reviews for other users");
            }
            return reviewService.getReviewsByUserId(userId, page, size);
        }

        return reviewService.getReviewsByUserId(authenticatedUserId, page, size);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ReviewResponse createReview(@Valid @RequestBody ReviewRequest request, Authentication authentication) {
        Long userId = securityService.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }
        return reviewService.createReview(request, userId, authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isUser(#userId, authentication)")
    public void deleteReview(@PathVariable Long id, @RequestParam Long userId, Authentication authentication) {
        reviewService.deleteReview(id, userId, authentication);
    }
}