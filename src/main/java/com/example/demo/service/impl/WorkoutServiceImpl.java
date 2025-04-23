package com.example.demo.service.impl;

import com.example.demo.dto.request.WorkoutRequest;
import com.example.demo.dto.response.WorkoutResponse;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.Workout;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkoutRepository;
import com.example.demo.service.WorkoutService;
import com.example.demo.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Autowired
    public WorkoutServiceImpl(
            WorkoutRepository workoutRepository,
            UserRepository userRepository
    ) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<WorkoutResponse> searchWorkouts(String keyword, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<Workout> workouts = workoutRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                keyword, keyword, pageRequest
        );
        return workouts.map(WorkoutResponse::new);
    }

    @Override
    public WorkoutResponse getWorkout(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout not found"));
        return new WorkoutResponse(workout);
    }

    @Override
    public WorkoutResponse createWorkout(WorkoutRequest workoutRequest) {
        User coach = userRepository.findById(workoutRequest.coachId())
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        Workout workout = new Workout();
        workout.setName(workoutRequest.name());
        workout.setCategory(workoutRequest.category());
        workout.setDescription(workoutRequest.description());
        workout.setDuration(workoutRequest.durationInMinutes());
        workout.setViewCount(0);
        workout.setCoach(coach);

        workoutRepository.save(workout);
        return new WorkoutResponse(workout);
    }

    @Override
    public WorkoutResponse updateWorkout(Long id, WorkoutRequest workoutRequest) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout not found"));

        if (!workout.getCoach().getId().equals(workoutRequest.coachId())) {
            User newCoach = userRepository.findById(workoutRequest.coachId())
                    .orElseThrow(() -> new RuntimeException("Coach not found"));
            workout.setCoach(newCoach);
        }

        workout.setName(workoutRequest.name());
        workout.setCategory(workoutRequest.category());
        workout.setDescription(workoutRequest.description());
        workout.setDuration(workoutRequest.durationInMinutes());

        workoutRepository.save(workout);
        return new WorkoutResponse(workout);
    }

    @Override
    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }
}