package com.example.demo.controller;

import com.example.demo.entity.Coach;
import com.example.demo.repository.CoachRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    private final CoachRepository coachRepository;

    public CoachController(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @GetMapping
    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    @PostMapping
    public Coach createCoach(@RequestBody Coach coach) {
        return coachRepository.save(coach);
    }

    @PutMapping("/{id}")
    public Coach updateCoach(@PathVariable Long id, @RequestBody Coach coach) {
        coach.setId(id);
        return coachRepository.save(coach);
    }

    @DeleteMapping("/{id}")
    public void deleteCoach(@PathVariable Long id) {
        coachRepository.deleteById(id);
    }
}
