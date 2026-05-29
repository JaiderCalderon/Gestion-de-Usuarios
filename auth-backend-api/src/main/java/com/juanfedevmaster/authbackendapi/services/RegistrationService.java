package com.juanfedevmaster.authbackendapi.services;

import org.springframework.stereotype.Service;

import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;
import com.juanfedevmaster.authbackendapi.exceptions.CedulaAlreadyExistsException;
import com.juanfedevmaster.authbackendapi.exceptions.EmailAlreadyExistsException;
import com.juanfedevmaster.authbackendapi.exceptions.UserAlreadyExistsException;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import com.juanfedevmaster.authbackendapi.security.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService implements IRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean register(RegisterUserRequest userToRegister) {
        if (userRepository.existsByUsername(userToRegister.getUsername()))
            throw new UserAlreadyExistsException("Username already exist: " + userToRegister.getUsername());

        if (userRepository.existsByEmail(userToRegister.getEmail()))
            throw new EmailAlreadyExistsException("Email already exist: " + userToRegister.getEmail());

        if (userRepository.existsByCedula(userToRegister.getCedula()))
            throw new CedulaAlreadyExistsException("Cedula already exist: " + userToRegister.getCedula());

        User user = User.builder()
                .username(userToRegister.getUsername())
                .password(passwordEncoder.encode(userToRegister.getPassword()))
                .email(userToRegister.getEmail())
                .cedula(userToRegister.getCedula())
                .build();

        userRepository.save(user);
        return true;
    }
}
