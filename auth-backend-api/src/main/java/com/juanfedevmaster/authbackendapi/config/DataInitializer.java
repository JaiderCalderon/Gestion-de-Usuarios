package com.juanfedevmaster.authbackendapi.config;

import com.juanfedevmaster.authbackendapi.entity.Role;
import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.repository.RoleRepository;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import com.juanfedevmaster.authbackendapi.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the default roles (USER, ADMIN) and a bootstrap admin account
 * so the application is usable out of the box.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(
                Role.builder().name("USER").description("Default user role").build()));

        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(
                Role.builder().name("ADMIN").description("Administrator role").build()));

        if (!userRepository.existsByEmail("admin@jas.com")) {
            userRepository.save(User.builder()
                    .cedula(1000000000L)
                    .name("Administrator")
                    .username("admin")
                    .email("admin@jas.com")
                    .password(passwordEncoder.encode("admin1234"))
                    .role(adminRole)
                    .enabled(true)
                    .build());
        }
    }
}
