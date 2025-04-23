package com.example.demo.model.entity;

import com.example.demo.model.enums.MembershipType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "memberships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipType type;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer duration;  // Renamed from `durationInDays` to match diagram
}