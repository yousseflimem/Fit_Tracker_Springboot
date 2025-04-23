package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateUserDto;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.enums.Role;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Membership;
import com.example.demo.model.entity.User;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user);
    }

    @Override
    public UserResponse create(CreateUserDto dto) {
        Membership membership = membershipRepository.findById(dto.getMembershipId())
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setMembership(membership);

        return new UserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse update(Long id, CreateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setRole(dto.getRole());

        if (dto.getMembershipId() != null) {
            Membership membership = membershipRepository.findById(dto.getMembershipId())
                    .orElseThrow(() -> new ResourceNotFoundException("Membership not found"));
            user.setMembership(membership);
        }

        return new UserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role);
        return new UserResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
