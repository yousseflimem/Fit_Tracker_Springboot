package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    private String status;

    // Payment for one Order (One-to-One)
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
