package com.example.demo.service.impl;

import com.example.demo.dto.request.PaymentRequest;
import com.example.demo.dto.response.PaymentResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.Payment;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;
import com.example.demo.service.SecurityService;
import com.example.demo.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final SecurityService securityService;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            SecurityService securityService
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.securityService = securityService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllPayments(int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        return paymentRepository.findAll(pageRequest).map(this::toPaymentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentsByUserId(Long userId, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        return paymentRepository.findByOrder_UserId(userId, pageRequest).map(this::toPaymentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return toPaymentResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request, Long userId, Authentication authentication) {
        // 1. Load the order
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.orderId()));

        // 2. Verify that the authenticated user owns this order
        if (!securityService.isOrderOwner(order.getId(), authentication)) {
            throw new SecurityException("You do not have permission to create a payment for this order");
        }

        // 3. Build & save the Payment entity
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setStatus(Payment.PaymentStatus.valueOf(request.status()));
        payment.setPaymentMethod("CARD");
        String full = request.cardNumber().replaceAll("\\s+", "");
        payment.setCardLast4(full.substring(full.length() - 4));
        paymentRepository.save(payment);

        // 4. **Update the Order’s status to COMPLETED**
        order.setStatus(com.example.demo.model.enums.OrderStatus.COMPLETED);
        orderRepository.save(order);

        // 5. Return the response DTO
        return toPaymentResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse updatePayment(Long id, PaymentRequest request, Long userId, Authentication authentication) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        // Verify order ownership
        if (!securityService.isOrderOwner(payment.getOrder().getId(), authentication)) {
            throw new SecurityException("You do not have permission to update this payment");
        }

        payment.setAmount(request.amount());
        payment.setPaymentDate(request.paymentDate());
        payment.setStatus(Payment.PaymentStatus.valueOf(request.status()));
        payment.setPaymentMethod("CARD");
        String full = request.cardNumber().replaceAll("\\s+", "");
        payment.setCardLast4(full.substring(full.length() - 4));

        paymentRepository.save(payment);
        return toPaymentResponse(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long id, Long userId, Authentication authentication) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        // Verify order ownership
        if (!securityService.isOrderOwner(payment.getOrder().getId(), authentication)) {
            throw new SecurityException("You do not have permission to delete this payment");
        }

        paymentRepository.deleteById(id);
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getStatus().name(),
                payment.getPaymentMethod(),
                payment.getCardLast4()
        );
    }
}
