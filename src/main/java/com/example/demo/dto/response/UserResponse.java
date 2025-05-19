package com.example.demo.dto.response;

import com.example.demo.model.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    //private final Long membershipId;
    //private final String membershipType;
    private final String profileImageUrl;

    public UserResponse(User u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.role = u.getRole().name();
        //this.membershipId = (u.getMembership() != null ? u.getMembership().getId() : null);
        //this.membershipType = (u.getMembership() != null ? u.getMembership().getType().name() : null);
        this.profileImageUrl = u.getProfileImageUrl();
    }
}