package com.example.demo.entity;

public class GymProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private double price;
    private String description;
    private int stock;
}