package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;
import com.juanfedevmaster.authbackendapi.exceptions.EmailAlreadyExistsException;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import com.juanfedevmaster.authbackendapi.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService implements IRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean register(RegisterUserRequest userToRegister) {
        if (userRepository.existsByEmail(userToRegister.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists: " + userToRegister.getEmail());

        User user = User.builder()
                .name(userToRegister.getName())
                .email(userToRegister.getEmail())
                .passwordHash(passwordEncoder.encode(userToRegister.getPassword()))
                .build();

        userRepository.save(user);
        return true;
    }
}
