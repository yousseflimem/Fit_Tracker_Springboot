package com.example.demo.service.impl;

import com.example.demo.dto.request.PaymentRequest;
import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.Payment;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toPaymentResponse)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return toPaymentResponse(payment);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        // Fetch the order
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Create payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setStatus(request.status());

        paymentRepository.save(payment);
        return toPaymentResponse(payment);
    }

    @Override
    public PaymentResponse updatePayment(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        // Update fields
        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setStatus(request.status());

        paymentRepository.save(payment);
        return toPaymentResponse(payment);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getStatus()
        );
    }
}