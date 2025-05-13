package com.example.demo.controller;

import com.example.demo.dto.request.ClassRequest;
import com.example.demo.dto.response.ClassResponse;
import com.example.demo.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    @Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping("/search")
    public Page<ClassResponse> searchClasses(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return classService.searchClasses(keyword, page, size);
    }

    @GetMapping("/{id}")
    public ClassResponse getClass(@PathVariable Long id) {
        return classService.getClass(id);
    }

    @GetMapping("/by-coach/{coachId}")
    @PreAuthorize("hasRole('ADMIN') or #coachId == @securityService.getUserIdFromAuthentication(authentication)")
    public Page<ClassResponse> getClassesByCoach(
            @PathVariable Long coachId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return classService.getClassesByCoach(coachId, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public ClassResponse createClass(@Valid @RequestBody ClassRequest classRequest) {
        return classService.createClass(classRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCoachForClass(#id, authentication)")
    public ClassResponse updateClass(
            @PathVariable Long id,
            @Valid @RequestBody ClassRequest classRequest
    ) {
        return classService.updateClass(id, classRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCoachForClass(#id, authentication)")
    public void deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
    }
}