package com.example.demo.controller;

import com.example.demo.dto.request.WorkoutRequest;
import com.example.demo.dto.response.WorkoutResponse;
import com.example.demo.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    public Page<WorkoutResponse> searchWorkouts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return workoutService.searchWorkouts(keyword, page, size);
    }

    @GetMapping("/{id}")
    public WorkoutResponse getWorkout(@PathVariable Long id) {
        return workoutService.getWorkout(id);
    }

    @PostMapping
    public WorkoutResponse createWorkout(@RequestBody WorkoutRequest workoutRequest) {
        return workoutService.createWorkout(workoutRequest);
    }

    @PutMapping("/{id}")
    public WorkoutResponse updateWorkout(
            @PathVariable Long id,
            @RequestBody WorkoutRequest workoutRequest
    ) {
        return workoutService.updateWorkout(id, workoutRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
    }
}