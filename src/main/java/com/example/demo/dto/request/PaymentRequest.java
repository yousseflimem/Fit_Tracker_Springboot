package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record PaymentRequest(
        @NotNull(message = "Order ID is required")
        Long orderId,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        Double amount,

        @NotNull(message = "Payment date is required")
        LocalDateTime paymentDate,

        @NotBlank(message = "Status is required")
        @Pattern(
                regexp = "PENDING|COMPLETED|FAILED",
                message = "Status must be PENDING, COMPLETED, or FAILED"
        )
        String status,

        // new fields for card data
        @NotBlank(message = "Card number is required")
        @Pattern(
                // allow groups of digits with or without spaces
                regexp = "^(?:\\d{4}\\s?){3}\\d{4}$",
                message = "Card number must be 16 digits"
        )
        String cardNumber,

        @NotBlank(message = "Expiry is required")
        @Pattern(
                regexp = "^(0[1-9]|1[0-2])/(?:\\d{2})$",
                message = "Expiry must be in MM/YY format"
        )
        String expiry,

        @NotBlank(message = "CVC is required")
        @Pattern(
                regexp = "^\\d{3}$",
                message = "CVC must be 3 digits"
        )
        String cvc
) {
    public PaymentRequest {
        // the @Pattern annotations handle format validation
    }
}
