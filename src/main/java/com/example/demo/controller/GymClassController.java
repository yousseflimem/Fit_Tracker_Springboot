package com.example.demo.controller;

import com.example.demo.entity.GymClass;
import com.example.demo.repository.GymClassRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/gymclasses")
public class GymClassController {

    private final GymClassRepository gymClassRepository;

    public GymClassController(GymClassRepository gymClassRepository) {
        this.gymClassRepository = gymClassRepository;
    }

    @GetMapping
    public List<GymClass> getAllGymClasses() {
        return gymClassRepository.findAll();
    }

    @PostMapping
    public GymClass createGymClass(@RequestBody GymClass gymClass) {
        return gymClassRepository.save(gymClass);
    }

    @PutMapping("/{id}")
    public GymClass updateGymClass(@PathVariable Long id, @RequestBody GymClass gymClass) {
        gymClass.setId(id);
        return gymClassRepository.save(gymClass);
    }

    @DeleteMapping("/{id}")
    public void deleteGymClass(@PathVariable Long id) {
        gymClassRepository.deleteById(id);
    }
}
