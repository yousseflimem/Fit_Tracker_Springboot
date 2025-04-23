package com.example.demo.dto.request;

import com.example.demo.model.enums.Role;
import lombok.Data;

@Data
public class CreateUserDto {
    private String username;
    private String email;
    private String password;
    private Role role;
    private Long membershipId;
}
