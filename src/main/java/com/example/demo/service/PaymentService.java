package com.example.demo.service;

import com.example.demo.dto.request.PaymentRequest;
import com.example.demo.dto.response.PaymentResponse;
import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(Long id);
    PaymentResponse createPayment(PaymentRequest paymentRequest);
    PaymentResponse updatePayment(Long id, PaymentRequest paymentRequest);
    void deletePayment(Long id);
}