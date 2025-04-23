// src/main/java/com/example/demo/model/entity/Workout.java
package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer duration;     // in minutes
    private Integer viewCount;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;

}
