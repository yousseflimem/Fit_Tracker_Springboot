package com.example.demo.dto.response;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        Double amount,
        LocalDateTime paymentDate,
        String status
) { }