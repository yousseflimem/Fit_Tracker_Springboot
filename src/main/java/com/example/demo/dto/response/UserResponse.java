package com.example.demo.dto.response;

import com.example.demo.model.entity.User;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Long membershipId;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.membershipId = user.getMembership() != null ? user.getMembership().getId() : null;
    }
}
