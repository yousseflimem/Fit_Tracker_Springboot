package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateUserDto;
import com.example.demo.dto.request.UpdateUserDto;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.enums.Role;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.entity.Membership;
import com.example.demo.model.entity.User;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //private final MembershipRepository membershipRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            //MembershipRepository membershipRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        //this.membershipRepository = membershipRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse create(CreateUserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken: " + dto.getUsername());
        }
        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already taken: " + dto.getEmail());
        }

//        Membership membership = null;
//        if (dto.getMembershipId() != null) {
//            membership = membershipRepository.findById(dto.getMembershipId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + dto.getMembershipId()));
//            if (membershipRepository.isMembershipAssigned(dto.getMembershipId())) {
//                throw new IllegalStateException("Membership already assigned to another user");
//            }
//        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.USER);
        //user.setMembership(membership);
        user.setProfileImageUrl(
                dto.getProfileImageUrl() != null ?
                        dto.getProfileImageUrl() :
                        "" // Default to empty string
        );
        return new UserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!dto.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken: " + dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already taken: " + dto.getEmail());
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.USER);
//        if (dto.getMembershipId() != null) {
//            Membership membership = membershipRepository.findById(dto.getMembershipId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + dto.getMembershipId()));
//            if (!membership.getId().equals(user.getMembership() != null ? user.getMembership().getId() : null) &&
//                    membershipRepository.isMembershipAssigned(dto.getMembershipId())) {
//                throw new IllegalStateException("Membership already assigned to another user");
//            }
//            user.setMembership(membership);
//        } else {
//            user.setMembership(null);
//        }
        user.setProfileImageUrl(dto.getProfileImageUrl());

        return new UserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setRole(role);
        return new UserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getCoaches() {
        return userRepository.findByRole(Role.COACH)
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}