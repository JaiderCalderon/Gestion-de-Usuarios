package com.juanfedevmaster.authbackendapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String name;

    @CreationTimestamp
    @Column(name = "antique", updatable = false)
    private LocalDateTime antique;

    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime updated;

    @Builder.Default
    private Boolean active = true;
}
