package com.example.demo.service.impl;

import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.request.OrderItemRequest;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.dto.response.OrderItemResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.*;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;
import com.example.demo.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAll(int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        return orderRepository.findAll(pageRequest).map(this::toOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(0.0);
        Order savedOrder = orderRepository.save(order);

        processOrderItems(request, savedOrder);
        updateOrderTotal(savedOrder);

        return toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse update(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Clear existing items
        orderItemRepository.deleteAll(order.getItems());
        order.getItems().clear();

        // Process new items
        processOrderItems(request, order);
        updateOrderTotal(order);

        return toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderItemRepository.deleteAll(order.getItems());
        orderRepository.delete(order);
    }

    private void processOrderItems(OrderRequest request, Order order) {
        double total = 0.0;
        for (OrderRequest.OrderItemRequest itemRequest : request.items()) {
            GymProduct product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + itemRequest.productId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            orderItemRepository.save(item);

            total += product.getPrice() * itemRequest.quantity();
            updateProductStock(product, itemRequest.quantity());
        }
        order.setTotalAmount(total);
    }

    private void updateProductStock(GymProduct product, int quantity) {
        int newStock = product.getStock() - quantity;
        if (newStock < 0) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }
        product.setStock(newStock);
        productRepository.save(product);
    }

    private void updateOrderTotal(Order order) {
        double total = order.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.COMPLETED);
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getUser().getId(),
                order.getUser().getEmail(),
                items
        );
    }
}