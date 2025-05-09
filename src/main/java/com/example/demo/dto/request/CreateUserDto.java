package com.example.demo.dto.request;

import com.example.demo.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CreateUserDto {
    private String username;
    private String email;
    private String password;
    private Role role;
    private Long membershipId;
}
