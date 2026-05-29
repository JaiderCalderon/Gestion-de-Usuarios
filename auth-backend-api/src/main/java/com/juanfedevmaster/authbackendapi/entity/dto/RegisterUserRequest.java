package com.juanfedevmaster.authbackendapi.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Cedula is required")
    private Long cedula;

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    private String email;
    
    @NotBlank(message = "password is required")
    private String password;
}
