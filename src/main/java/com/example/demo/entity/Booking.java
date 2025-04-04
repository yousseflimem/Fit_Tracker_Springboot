package com.example.demo.entity;

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private LocalDateTime bookingDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private GymClass gymClass;

    @OneToOne(mappedBy = "booking")
    private Payment payment;
}