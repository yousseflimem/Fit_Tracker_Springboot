// src/main/java/com/example/demo/repository/PaymentRepository.java
package com.example.demo.repository;

import com.example.demo.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> { }
