package com.example.demo.entity;

public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutId;
    private String name;
    private String description;
    private String difficulty;
    private int duration;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;
}