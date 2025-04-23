package com.example.demo.dto.request;

import java.time.LocalDateTime;

public record PaymentRequest(
        Long orderId,      // Required to link to Order
        Double amount,
        LocalDateTime paymentDate,
        String status      // e.g., "COMPLETED"
) { }