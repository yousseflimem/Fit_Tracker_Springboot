package com.example.demo.service;

import com.example.demo.dto.request.CreateUserDto;
import com.example.demo.dto.request.UpdateUserDto;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.enums.Role;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();
    UserResponse getById(Long id);
    UserResponse create(CreateUserDto dto);
    UserResponse update(Long id,  UpdateUserDto dto);
    UserResponse updateRole(Long id, Role role);
    void delete(Long id);
    List<UserResponse> getCoaches(); // New method
}