package com.juanfedevmaster.authbackendapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RoleResponse {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime created;
}
