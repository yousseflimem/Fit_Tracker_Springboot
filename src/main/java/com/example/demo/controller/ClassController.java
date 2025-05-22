package com.example.demo.controller;

import com.example.demo.dto.request.ClassRequest;
import com.example.demo.dto.response.ClassResponse;
import com.example.demo.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<ClassResponse>> searchClasses(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(classService.searchClasses(keyword, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassResponse> getClass(@PathVariable Long id) {
        return ResponseEntity.ok(classService.getClass(id));
    }

    @GetMapping("/by-coach/{coachId}")
    public Page<ClassResponse> getClassesByCoach(
            @PathVariable Long coachId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return classService.getClassesByCoach(coachId, page, size);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public ResponseEntity<ClassResponse> createClass(@Valid @RequestBody ClassRequest classRequest) {
        return ResponseEntity.ok(classService.createClass(classRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCoachForClass(#id, authentication)")
    public ResponseEntity<ClassResponse> updateClass(
            @PathVariable Long id,
            @Valid @RequestBody ClassRequest classRequest) {
        return ResponseEntity.ok(classService.updateClass(id, classRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCoachForClass(#id, authentication)")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }

}