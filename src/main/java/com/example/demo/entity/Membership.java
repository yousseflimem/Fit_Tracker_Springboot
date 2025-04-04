package com.example.demo.entity;

public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;
    private String type;
    private double price;
    private int duration;

    @OneToMany(mappedBy = "membership")
    private List<User> users;
}