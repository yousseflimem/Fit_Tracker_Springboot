package com.example.demo.service.impl;

import com.example.demo.dto.request.ClassRequest;
import com.example.demo.dto.response.ClassResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.GymClass;
import com.example.demo.model.entity.Image;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.Workout;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkoutRepository;
import com.example.demo.service.ClassService;
import com.example.demo.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {
    private static final Logger logger = LoggerFactory.getLogger(ClassServiceImpl.class);

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public ClassServiceImpl(
            ClassRepository classRepository,
            UserRepository userRepository,
            WorkoutRepository workoutRepository
    ) {
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassResponse> searchClasses(String keyword, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<GymClass> classes = classRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                keyword, keyword, pageRequest
        );
        return classes.map(ClassResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassResponse getClass(Long id) {
        GymClass gymClass = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + id));
        return new ClassResponse(gymClass);
    }

    @Override
    @Transactional
    public ClassResponse createClass(ClassRequest classRequest) {
        logger.info("Creating gym class with title: {}", classRequest.title());

        User coach = userRepository.findById(classRequest.coachId())
                .orElseThrow(() -> new ResourceNotFoundException("Coach not found with id: " + classRequest.coachId()));

        List<Workout> workouts = classRequest.workoutIds().stream()
                .map(workoutId -> workoutRepository.findById(workoutId)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + workoutId)))
                .collect(Collectors.toList());

        GymClass gymClass = new GymClass();
        gymClass.setTitle(classRequest.title());
        gymClass.setCategory(classRequest.category());
        gymClass.setDescription(classRequest.description());
        gymClass.setStartTime(classRequest.startTime());
        gymClass.setEndTime(classRequest.endTime());
        gymClass.setCapacity(classRequest.capacity());
        gymClass.setCoach(coach);
        gymClass.setWorkouts(workouts);

        List<Image> images = classRequest.imageUrls() != null
                ? classRequest.imageUrls().stream()
                .filter(url -> url != null && !url.trim().isEmpty())
                .map(url -> {
                    Image image = new Image();
                    image.setUrl(url);
                    return image;
                })
                .collect(Collectors.toList())
                : List.of();
        gymClass.setImages(images);

        try {
            GymClass savedClass = classRepository.save(gymClass);
            logger.info("Successfully created gym class with id: {}", savedClass.getId());
            return new ClassResponse(savedClass);
        } catch (Exception e) {
            logger.error("Failed to create gym class with title: {}", classRequest.title(), e);
            throw new RuntimeException("Failed to create class: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ClassResponse updateClass(Long id, ClassRequest classRequest) {
        logger.info("Updating gym class with id: {}", id);

        GymClass gymClass = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + id));

        if (!gymClass.getCoach().getId().equals(classRequest.coachId())) {
            User newCoach = userRepository.findById(classRequest.coachId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coach not found with id: " + classRequest.coachId()));
            gymClass.setCoach(newCoach);
        }

        List<Workout> workouts = classRequest.workoutIds().stream()
                .map(workoutId -> workoutRepository.findById(workoutId)
                        .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + workoutId)))
                .collect(Collectors.toList());

        gymClass.setTitle(classRequest.title());
        gymClass.setCategory(classRequest.category());
        gymClass.setDescription(classRequest.description());
        gymClass.setStartTime(classRequest.startTime());
        gymClass.setEndTime(classRequest.endTime());
        gymClass.setCapacity(classRequest.capacity());
        gymClass.setWorkouts(workouts);

        gymClass.getImages().clear();
        List<Image> images = classRequest.imageUrls() != null
                ? classRequest.imageUrls().stream()
                .filter(url -> url != null && !url.trim().isEmpty())
                .map(url -> {
                    Image image = new Image();
                    image.setUrl(url);
                    return image;
                })
                .toList()
                : List.of();
        gymClass.getImages().addAll(images);

        try {
            GymClass savedClass = classRepository.save(gymClass);
            logger.info("Successfully updated gym class with id: {}", id);
            return new ClassResponse(savedClass);
        } catch (Exception e) {
            logger.error("Failed to update gym class with id: {}", id, e);
            throw new RuntimeException("Failed to update class: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassResponse> getClassesByCoach(Long coachId, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<GymClass> classes = classRepository.findByCoachId(coachId, pageRequest);
        return classes.map(ClassResponse::new);
    }

    @Override
    @Transactional
    public void deleteClass(Long id) {
        if (!classRepository.existsById(id)) {
            throw new ResourceNotFoundException("Class not found with id: " + id);
        }
        classRepository.deleteById(id);
    }
}