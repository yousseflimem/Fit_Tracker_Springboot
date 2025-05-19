package com.example.demo.dto.request;

import com.example.demo.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password; // Make password optional

    private Role role;
    //private Long membershipId;

    @Pattern(regexp = "^(https?://.*\\.(?:png|jpg|jpeg|gif))$|^$",
            message = "Profile image URL must be a valid image URL or empty")
    private String profileImageUrl;
}