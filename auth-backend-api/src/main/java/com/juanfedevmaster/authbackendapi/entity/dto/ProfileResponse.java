package com.juanfedevmaster.authbackendapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private Integer id;
    private Integer userId;
    private String phone;
    private String company;
    private String biography;
    private String avatarUrl;
    private LocalDateTime updated;
}
