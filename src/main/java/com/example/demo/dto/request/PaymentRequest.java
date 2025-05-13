package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record PaymentRequest(
        @NotNull(message = "Order ID is required") Long orderId,
        @NotNull(message = "Amount is required") @Positive(message = "Amount must be positive") Double amount,
        @NotNull(message = "Payment date is required") LocalDateTime paymentDate,
        @NotNull(message = "Status is required") String status // e.g., "PENDING", "COMPLETED", "FAILED"
) {
    public PaymentRequest {
        if (status != null && !status.matches("PENDING|COMPLETED|FAILED")) {
            throw new IllegalArgumentException("Status must be PENDING, COMPLETED, or FAILED");
        }
    }
}