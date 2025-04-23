package com.example.demo.dto.response;

public record OrderItemResponse(
        Long productId,
        String productName,
        Double price,
        Integer quantity
) { }
