package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record OrderRequest(
        @NotEmpty(message = "At least one item is required") List<OrderItemRequest> items
) {
    public record OrderItemRequest(
            @NotNull(message = "Product ID is required") Long productId,
            @Positive(message = "Quantity must be positive") Integer quantity
    ) {}
}