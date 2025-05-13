package com.example.demo.service.impl;

import com.example.demo.dto.request.WorkoutRequest;
import com.example.demo.dto.response.WorkoutResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Image;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.Workout;
import com.example.demo.model.enums.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkoutRepository;
import com.example.demo.service.WorkoutService;
import com.example.demo.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public WorkoutServiceImpl(
            WorkoutRepository workoutRepository,
            UserRepository userRepository
    ) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutResponse> searchWorkouts(String keyword, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<Workout> workouts = workoutRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                keyword, keyword, pageRequest
        );
        return workouts.map(WorkoutResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutResponse getWorkout(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
        return new WorkoutResponse(workout);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutResponse> getWorkoutsByCoach(Long coachId, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<Workout> workouts = workoutRepository.findByCoachId(coachId, pageRequest);
        return workouts.map(WorkoutResponse::new);
    }

    @Override
    @Transactional
    public WorkoutResponse createWorkout(WorkoutRequest workoutRequest) {
        User coach = userRepository.findById(workoutRequest.coachId())
                .orElseThrow(() -> new ResourceNotFoundException("Coach not found with id: " + workoutRequest.coachId()));
        if (!coach.getRole().equals(Role.COACH)) {
            throw new IllegalArgumentException("User is not a coach: " + workoutRequest.coachId());
        }

        Workout workout = new Workout();
        workout.setName(workoutRequest.name());
        workout.setCategory(workoutRequest.category());
        workout.setDescription(workoutRequest.description());
        workout.setDuration(workoutRequest.durationInMinutes());
        workout.setViewCount(0);
        workout.setCoach(coach);

        List<Image> images = workoutRequest.imageUrls().stream()
                .map(url -> {
                    Image image = new Image();
                    image.setUrl(url);
                    return image;
                })
                .toList();
        workout.setImages(images);

        workoutRepository.save(workout);
        return new WorkoutResponse(workout);
    }

    @Override
    @Transactional
    public WorkoutResponse updateWorkout(Long id, WorkoutRequest workoutRequest) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        User coach = userRepository.findById(workoutRequest.coachId())
                .orElseThrow(() -> new ResourceNotFoundException("Coach not found with id: " + workoutRequest.coachId()));
        if (!coach.getRole().equals(Role.COACH)) {
            throw new IllegalArgumentException("User is not a coach: " + workoutRequest.coachId());
        }

        workout.setName(workoutRequest.name());
        workout.setCategory(workoutRequest.category());
        workout.setDescription(workoutRequest.description());
        workout.setDuration(workoutRequest.durationInMinutes());
        workout.setCoach(coach);

        // Clear existing images to avoid persistence issues
        workout.getImages().clear();

        // Add new images
        List<Image> images = workoutRequest.imageUrls().stream()
                .map(url -> {
                    Image image = new Image();
                    image.setUrl(url);
                    return image;
                })
                .toList();
        workout.getImages().addAll(images);

        workoutRepository.save(workout);
        return new WorkoutResponse(workout);
    }

    @Override
    @Transactional
    public void deleteWorkout(Long id) {
        if (!workoutRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workout not found with id: " + id);
        }
        workoutRepository.deleteById(id);
    }
}