package com.example.demo.service.impl;

import com.example.demo.dto.request.ClassRequest;
import com.example.demo.dto.response.ClassResponse;
import com.example.demo.model.entity.GymClass;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ClassRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ClassService;
import com.example.demo.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClassServiceImpl(ClassRepository classRepository, UserRepository userRepository) {
        this.classRepository = classRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<ClassResponse> searchClasses(String keyword, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        Page<GymClass> classes = classRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                keyword, keyword, pageRequest
        );
        return classes.map(ClassResponse::new);
    }

    @Override
    public ClassResponse getClass(Long id) {
        GymClass gymClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        return new ClassResponse(gymClass);
    }

    @Override
    public ClassResponse createClass(ClassRequest classRequest) {
        // Fetch coach
        User coach = userRepository.findById(classRequest.coachId())
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        // Build entity
        GymClass gymClass = new GymClass();
        gymClass.setTitle(classRequest.title());
        gymClass.setCategory(classRequest.category());
        gymClass.setDescription(classRequest.description());
        gymClass.setStartTime(classRequest.startTime());
        gymClass.setEndTime(classRequest.endTime());
        gymClass.setCapacity(classRequest.capacity());
        gymClass.setCoach(coach);
        gymClass.setImageUrl(classRequest.imageUrl());

        // Save and return response
        classRepository.save(gymClass);
        return new ClassResponse(gymClass);
    }

    @Override
    public ClassResponse updateClass(Long id, ClassRequest classRequest) {
        GymClass gymClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Update coach if changed
        if (!gymClass.getCoach().getId().equals(classRequest.coachId())) {
            User newCoach = userRepository.findById(classRequest.coachId())
                    .orElseThrow(() -> new RuntimeException("Coach not found"));
            gymClass.setCoach(newCoach);
        }

        // Update other fields
        gymClass.setTitle(classRequest.title());
        gymClass.setCategory(classRequest.category());
        gymClass.setDescription(classRequest.description());
        gymClass.setStartTime(classRequest.startTime());
        gymClass.setEndTime(classRequest.endTime());
        gymClass.setCapacity(classRequest.capacity());
        gymClass.setImageUrl(classRequest.imageUrl());

        classRepository.save(gymClass);
        return new ClassResponse(gymClass);
    }

    @Override
    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }
}