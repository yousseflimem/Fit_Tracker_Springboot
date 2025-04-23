package com.example.demo.dto.response;

import com.example.demo.model.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
    private Long workoutId;
    private String workoutName;

    public ReviewResponse(Review r) {
        this.id = r.getId();
        this.rating = r.getRating();
        this.comment = r.getComment();
        this.createdAt = r.getCreatedAt();
        this.userId = r.getUser().getId();
        this.username = r.getUser().getUsername();
        this.workoutId = r.getWorkout().getId();
        this.workoutName = r.getWorkout().getName();
    }
}
