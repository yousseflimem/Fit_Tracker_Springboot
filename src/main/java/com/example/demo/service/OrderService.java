package com.example.demo.service;

import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface OrderService {
    Page<OrderResponse> getAll(int page, int size);
    Page<OrderResponse> getByUserId(Long userId, int page, int size); // New method
    OrderResponse getById(Long id);
    OrderResponse create(OrderRequest request, Long userId, Authentication authentication);
    OrderResponse update(Long id, OrderRequest request, Long userId, Authentication authentication);
    void deleteOrder(Long id);
    OrderResponse updateStatus(Long id, String status, Authentication authentication);

}