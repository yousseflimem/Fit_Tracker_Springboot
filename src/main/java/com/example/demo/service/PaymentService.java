package com.example.demo.service;

import com.example.demo.dto.request.PaymentRequest;
import com.example.demo.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    Page<PaymentResponse> getAllPayments(int page, int size);
    Page<PaymentResponse> getPaymentsByUserId(Long userId, int page, int size);
    PaymentResponse getPaymentById(Long id);
    PaymentResponse createPayment(PaymentRequest paymentRequest, Long userId, Authentication authentication);
    PaymentResponse updatePayment(Long id, PaymentRequest paymentRequest, Long userId, Authentication authentication);
    void deletePayment(Long id, Long userId, Authentication authentication);
}