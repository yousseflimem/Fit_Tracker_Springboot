package com.example.demo.dto.request;

import java.util.List;

public record OrderRequest(
        Long userId,
        List<OrderItemRequest> items
) {
    public record OrderItemRequest(
            Long productId,
            Integer quantity
    ) {}
}  // Fixed closing brace