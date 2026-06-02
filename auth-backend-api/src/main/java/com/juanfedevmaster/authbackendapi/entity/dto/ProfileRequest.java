package com.juanfedevmaster.authbackendapi.entity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileRequest {

    @NotNull(message = "User id is required")
    private Integer userId;

    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @Size(max = 150, message = "Company must be at most 150 characters")
    private String company;

    private String biography;

    @Size(max = 500, message = "Avatar url must be at most 500 characters")
    private String avatarUrl;
}
