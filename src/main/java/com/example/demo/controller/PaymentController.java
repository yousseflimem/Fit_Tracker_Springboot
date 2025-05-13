package com.example.demo.controller;

import com.example.demo.dto.request.PaymentRequest;
import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.service.PaymentService;
import com.example.demo.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityService securityService;

    public PaymentController(PaymentService paymentService, SecurityService securityService) {
        this.paymentService = paymentService;
        this.securityService = securityService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<PaymentResponse> getAllPayments(@RequestParam int page, @RequestParam int size) {
        return paymentService.getAllPayments(page, size);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public Page<PaymentResponse> getUserPayments(
            @RequestParam(required = false) Long userId,
            @RequestParam int page,
            @RequestParam int size,
            Authentication authentication
    ) {
        Long authenticatedUserId = securityService.getUserIdFromAuthentication(authentication);
        if (authenticatedUserId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }

        if (userId != null) {
            if (!authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                throw new SecurityException("Only admins can view payments for other users");
            }
            return paymentService.getPaymentsByUserId(userId, page, size);
        }

        return paymentService.getPaymentsByUserId(authenticatedUserId, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrderOwner(#id, authentication)")
    public PaymentResponse getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public PaymentResponse createPayment(@Valid @RequestBody PaymentRequest request, Authentication authentication) {
        Long userId = securityService.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }
        return paymentService.createPayment(request, userId, authentication);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrderOwner(#id, authentication)")
    public PaymentResponse updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request,
            Authentication authentication
    ) {
        Long userId = securityService.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }
        return paymentService.updatePayment(id, request, userId, authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrderOwner(#id, authentication)")
    public void deletePayment(@PathVariable Long id, Authentication authentication) {
        Long userId = securityService.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }
        paymentService.deletePayment(id, userId, authentication);
    }
}