package com.example.demo.controller;

import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.service.OrderService;
import com.example.demo.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrderOwner(#id, authentication)")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest orderRequest, Authentication authentication) {
        Long userId = securityService.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }
        return orderService.create(orderRequest, userId, authentication);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrderOwner(#id, authentication)")
    public OrderResponse updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest orderRequest, Authentication authentication) {
        Long userId = securityService.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }
        return orderService.update(id, orderRequest, userId, authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOrderOwner(#id, authentication)")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderResponse> getAllOrders(@RequestParam int page, @RequestParam int size) {
        return orderService.getAll(page, size);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public Page<OrderResponse> getUserOrders(
            @RequestParam(required = false) Long userId,
            @RequestParam int page,
            @RequestParam int size,
            Authentication authentication
    ) {
        Long authenticatedUserId = securityService.getUserIdFromAuthentication(authentication);
        if (authenticatedUserId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }

        // If userId is provided, only admins can access orders for another user
        if (userId != null) {
            if (!authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                throw new SecurityException("Only admins can view orders for other users");
            }
            return orderService.getByUserId(userId, page, size);
        }

        // If no userId is provided, return orders for the authenticated user
        return orderService.getByUserId(authenticatedUserId, page, size);
    }
}