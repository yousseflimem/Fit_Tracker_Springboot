package com.example.demo.service;

import com.example.demo.dto.request.WorkoutRequest;
import com.example.demo.dto.response.WorkoutResponse;
import org.springframework.data.domain.Page;

public interface WorkoutService {
    Page<WorkoutResponse> searchWorkouts(String keyword, int page, int size);
    WorkoutResponse getWorkout(Long id);
    WorkoutResponse createWorkout(WorkoutRequest workoutRequest);
    WorkoutResponse updateWorkout(Long id, WorkoutRequest workoutRequest);
    void deleteWorkout(Long id);
}
