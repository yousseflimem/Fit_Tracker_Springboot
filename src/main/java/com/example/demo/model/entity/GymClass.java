package com.example.demo.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gym_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GymClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String category;
    private String description;

    @ElementCollection
    @CollectionTable(name = "gym_class_images", joinColumns = @JoinColumn(name = "gym_class_id"))
    @OrderColumn(name = "image_order")
    private List<Image> images = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Integer duration; // in minutes

    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gym_class_workouts",
            joinColumns = @JoinColumn(name = "gym_class_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id")
    )
    @Size(min = 1, message = "At least one workout is required")
    private List<Workout> workouts = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void calculateDuration() {
        if (startTime != null && endTime != null) {
            if (endTime.isBefore(startTime)) {
                throw new IllegalArgumentException("End time must be after start time");
            }
            this.duration = (int) Duration.between(startTime, endTime).toMinutes();
        }
    }
}