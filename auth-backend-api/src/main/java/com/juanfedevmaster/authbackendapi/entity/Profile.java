package com.juanfedevmaster.authbackendapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(length = 150)
    private String company;

    @Column(columnDefinition = "text")
    private String biography;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime updated;
}
