package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "workouts")
@Getter
@Setter
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String difficulty;
    private Integer duration; // Duration in minutes

    // Each Workout is led by one Coach
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;
}
