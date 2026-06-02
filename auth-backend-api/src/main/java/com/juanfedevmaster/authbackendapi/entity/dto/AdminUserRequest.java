package com.juanfedevmaster.authbackendapi.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminUserRequest {

    @NotNull(message = "ID card required")
    @Positive(message = "The ID number must be a positive number")
    private Long cedula;

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

    @NotBlank(message = "The password is required")
    @Size(min = 8, message = "The password must have at least 8 characters")
    private String password;

    @NotBlank(message = "The role is required")
    private String role;
}
