package com.example.demo.service.impl;

import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.response.OrderItemResponse;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.*;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;
import com.example.demo.service.SecurityService;
import com.example.demo.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final SecurityService securityService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderItemRepository orderItemRepository,
            SecurityService securityService
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.securityService = securityService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAll(int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        return orderRepository.findAll(pageRequest).map(this::toOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getByUserId(Long userId, int page, int size) {
        PageRequest pageRequest = PaginationUtil.createPageRequest(page, size);
        return orderRepository.findByUserId(userId, pageRequest).map(this::toOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request, Long userId, Authentication authentication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(0.0);
        Order savedOrder = orderRepository.save(order);

        double total = processOrderItems(request.items(), savedOrder);
        savedOrder.setTotalAmount(total);
        orderRepository.save(savedOrder); // Stays PENDING

        return toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse update(Long id, OrderRequest request, Long userId, Authentication authentication) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        order.setUser(user);

        orderItemRepository.deleteAll(order.getItems());
        order.getItems().clear();

        double total = processOrderItems(request.items(), order);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.COMPLETED);

        return toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        orderItemRepository.deleteAll(order.getItems());
        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, String status, Authentication authentication) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        // Only owner or admin can change status
        if (!securityService.isOrderOwner(id, authentication)
                && authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Not authorized to change order status");
        }

        // Validate status
        OrderStatus newStatus = OrderStatus.valueOf(status);
        order.setStatus(newStatus);
        orderRepository.save(order);

        return toOrderResponse(order);
    }

    private double processOrderItems(List<OrderRequest.OrderItemRequest> itemRequests, Order order) {
        double total = 0.0;
        List<OrderItem> items = new ArrayList<>();
        for (OrderRequest.OrderItemRequest itemRequest : itemRequests) {
            GymProduct product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.productId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            items.add(item);

            total += product.getPrice() * itemRequest.quantity();
            updateProductStock(product, itemRequest.quantity());
        }
        order.getItems().addAll(items);
        orderItemRepository.saveAll(items);
        return total;
    }

    private void updateProductStock(GymProduct product, int quantity) {
        int newStock = product.getStock() - quantity;
        if (newStock < 0) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }
        product.setStock(newStock);
        productRepository.save(product);
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
                order.getUser().getProfileImageUrl(),
                items
        );
    }
}