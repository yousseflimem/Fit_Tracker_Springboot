package com.example.demo.controller;

import com.example.demo.entity.Workout;
import com.example.demo.repository.WorkoutRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutRepository workoutRepository;

    public WorkoutController(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @GetMapping
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    @PostMapping
    public Workout createWorkout(@RequestBody Workout workout) {
        return workoutRepository.save(workout);
    }

    @PutMapping("/{id}")
    public Workout updateWorkout(@PathVariable Long id, @RequestBody Workout workout) {
        workout.setId(id);
        return workoutRepository.save(workout);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkout(@PathVariable Long id) {
        workoutRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public Workout getWorkoutById(@PathVariable Long id) {
        return workoutRepository.findWorkoutById(id).orElse(null);
    }
}
