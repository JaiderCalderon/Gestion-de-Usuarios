package com.juanfedevmaster.authbackendapi.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminUserUpdateRequest {

    @NotBlank(message = "The name is required")
    @Size(max = 100, message = "The name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "The username cannot exceed 50 characters")
    private String username;

    @NotBlank(message = "The email is required")
    @Email(message = "The email is not in a valid format")
    @Size(max = 150, message = "The email cannot exceed 150 characters")
    private String email;

    @NotBlank(message = "The role is required")
    private String role;

    private Boolean enabled;
}
