package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.Role;
import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;
import com.juanfedevmaster.authbackendapi.exceptions.EmailAlreadyExistsException;
import com.juanfedevmaster.authbackendapi.repository.RoleRepository;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import com.juanfedevmaster.authbackendapi.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService implements IRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean register(RegisterUserRequest userToRegister) {
        if (userRepository.existsByEmail(userToRegister.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists: " + userToRegister.getEmail());

        // Get or create default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("USER")
                        .description("Default user role")
                        .build()));

        User user = User.builder()
                .name(userToRegister.getName())
                .email(userToRegister.getEmail())
                .password(passwordEncoder.encode(userToRegister.getPassword()))
                .role(userRole)
                .build();

        userRepository.save(user);
        return true;
    }
}
