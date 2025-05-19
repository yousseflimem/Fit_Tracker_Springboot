package com.example.demo.controller;

import com.example.demo.dto.request.CreateUserDto;
import com.example.demo.dto.request.UpdateUserDto;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.enums.Role;
import com.example.demo.service.SecurityService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isUser(#id, authentication)")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserResponse getCurrentUser(Authentication authentication) {
        Long userId = securityService.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new IllegalStateException("Authenticated user ID not found");
        }
        return userService.getById(userId);
    }

    @GetMapping("/coaches")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH') or hasRole('USER')")
    public List<UserResponse> getCoaches() {
        return userService.getCoaches();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse create(@Valid @RequestBody CreateUserDto dto) {
        return userService.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isUser(#id, authentication)")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        return userService.update(id, dto);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateRole(@PathVariable Long id, @RequestBody Role role) {
        return userService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}