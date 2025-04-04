package com.example.demo.entity;

public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coachId;
    private String name;
    private int experience;

    @ElementCollection
    private List<String> specialties;

    private String bio;

    @OneToMany(mappedBy = "coach")
    private List<Workout> workouts;

    @OneToMany(mappedBy = "coach")
    private List<GymClass> gymClasses;
}

