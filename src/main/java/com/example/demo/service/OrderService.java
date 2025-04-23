package com.example.demo.service;


import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.response.OrderResponse;
import org.springframework.data.domain.Page;


public interface OrderService {
    Page<OrderResponse> getAll(int page, int size);
    OrderResponse getById(Long id);
    OrderResponse create(OrderRequest request);
    OrderResponse update(Long id, OrderRequest request);
    void deleteOrder(Long id); // Changed from delete()
    // Remove other undefined methods if present
}
