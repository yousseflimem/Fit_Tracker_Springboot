package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workouts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Workout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;
    private String description;
    private Integer duration; // in minutes
    private Integer viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    @ElementCollection
    @CollectionTable(name = "workout_images", joinColumns = @JoinColumn(name = "workout_id"))
    @OrderColumn(name = "image_order")
    private List<Image> images = new ArrayList<>();
}