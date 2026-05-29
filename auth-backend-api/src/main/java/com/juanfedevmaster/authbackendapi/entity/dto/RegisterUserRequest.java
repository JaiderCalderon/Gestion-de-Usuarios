package com.juanfedevmaster.authbackendapi.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotNull(message = "Cedula is required")
    private Long cedula;

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;
    
    @NotBlank(message = "password is required")
    private String password;
}
