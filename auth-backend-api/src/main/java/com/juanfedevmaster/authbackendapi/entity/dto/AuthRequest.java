package com.juanfedevmaster.authbackendapi.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "The email is required")
    @Email(message = "The email is not in a valid format.")
    private String email;

    @NotBlank(message = "The password is required")
    private String password;
}