package com.example.demo.service;

import com.example.demo.dto.request.PaymentRequest;
import com.example.demo.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    Page<PaymentResponse> getAllPayments(int page, int size);
    Page<PaymentResponse> getPaymentsByUserId(Long userId, int page, int size);
    PaymentResponse getPaymentById(Long id);
    PaymentResponse createPayment(PaymentRequest request, Long userId, Authentication auth);
    PaymentResponse updatePayment(Long id, PaymentRequest request, Long userId, Authentication auth);
    void deletePayment(Long id, Long userId, Authentication auth);
}
