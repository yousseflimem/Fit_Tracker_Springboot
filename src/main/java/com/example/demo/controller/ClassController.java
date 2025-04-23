package com.example.demo.controller;

import com.example.demo.dto.request.ClassRequest;
import com.example.demo.dto.response.ClassResponse;
import com.example.demo.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    @Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
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

    @PostMapping
    public ClassResponse createClass(@RequestBody ClassRequest classRequest) {
        return classService.createClass(classRequest);
    }

    @PutMapping("/{id}")
    public ClassResponse updateClass(
            @PathVariable Long id,
            @RequestBody ClassRequest classRequest
    ) {
        return classService.updateClass(id, classRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
    }
}