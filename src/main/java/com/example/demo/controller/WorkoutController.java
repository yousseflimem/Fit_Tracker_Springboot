package com.example.demo.controller;

import com.example.demo.dto.request.WorkoutRequest;
import com.example.demo.dto.response.WorkoutResponse;
import com.example.demo.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

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

    @GetMapping("/by-coach/{coachId}")
    @PreAuthorize("hasRole('ADMIN') or #coachId == @securityService.getUserIdFromAuthentication(authentication)")
    public Page<WorkoutResponse> getWorkoutsByCoach(
            @PathVariable Long coachId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return workoutService.getWorkoutsByCoach(coachId, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public WorkoutResponse createWorkout(@Valid @RequestBody WorkoutRequest workoutRequest) {
        return workoutService.createWorkout(workoutRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCoachForWorkout(#id, authentication)")
    public WorkoutResponse updateWorkout(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutRequest workoutRequest
    ) {
        return workoutService.updateWorkout(id, workoutRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCoachForWorkout(#id, authentication)")
    public void deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
    }
}