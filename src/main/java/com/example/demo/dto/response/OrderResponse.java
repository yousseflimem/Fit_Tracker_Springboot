package com.example.demo.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        LocalDateTime orderDate,
        Double totalAmount,
        String status,
        Long userId,
        String userEmail,
        List<OrderItemResponse> items
) {}  // Fixed closing brace