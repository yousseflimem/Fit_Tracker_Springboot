package com.example.demo.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long userId;
    private Long workoutId;
    private Integer rating;
    private String comment;
}
