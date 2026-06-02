package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.Role;
import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.AuthResponse;
import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;
import com.juanfedevmaster.authbackendapi.exceptions.EmailAlreadyExistsException;
import com.juanfedevmaster.authbackendapi.repository.RoleRepository;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import com.juanfedevmaster.authbackendapi.security.JwtUtil;
import com.juanfedevmaster.authbackendapi.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService implements IRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterUserRequest userToRegister) {
        if (userRepository.existsByEmail(userToRegister.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists: " + userToRegister.getEmail());
        if (userRepository.existsByUsername(userToRegister.getUsername()))
            throw new EmailAlreadyExistsException("Username already exists: " + userToRegister.getUsername());
        if (userRepository.existsByCedula(userToRegister.getCedula()))
            throw new EmailAlreadyExistsException("ID card already registered: " + userToRegister.getCedula());

        // Get or create default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("USER")
                        .description("Default user role")
                        .build()));

        User user = User.builder()
                .cedula(userToRegister.getCedula())
                .name(userToRegister.getName())
                .username(userToRegister.getUsername())
                .email(userToRegister.getEmail())
                .password(passwordEncoder.encode(userToRegister.getPassword()))
                .role(userRole)
                .enabled(true)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), userRole.getName());
        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(userRole.getName())
                .build();
    }
}
