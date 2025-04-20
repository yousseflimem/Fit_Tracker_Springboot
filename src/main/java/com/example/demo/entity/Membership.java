package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "memberships")
@Getter
@Setter
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;      // e.g., "Basic", "Premium"
    private Double price;
    private Integer duration; // Duration in days
}
