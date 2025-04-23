package com.example.demo.dto.request;

public record OrderItemRequest(
        Long productId,
        Integer quantity
) { }
