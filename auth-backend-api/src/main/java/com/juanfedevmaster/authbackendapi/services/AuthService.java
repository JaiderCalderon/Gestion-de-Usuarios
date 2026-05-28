package com.juanfedevmaster.authbackendapi.services;

import org.springframework.stereotype.Service;

import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.AuthRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public boolean register(RegisterUserRequest userToRegister) {
        if (userRepository.existsByUsername(userToRegister.getUsername()))
            throw new IllegalArgumentException("Username alredy exist: " + userToRegister.getUsername());

        if (userRepository.existsByEmail(userToRegister.getEmail()))
            throw new IllegalArgumentException("Email alredy exist: " + userToRegister.getEmail());

        if (userRepository.existsByCedula(userToRegister.getCedula()))
            throw new IllegalArgumentException("Email alredy exist: " + userToRegister.getCedula());

        User user = User.builder()
                .username(userToRegister.getUsername())
                .password(userToRegister.getPassword())
                .email(userToRegister.getEmail())
                .cedula(userToRegister.getCedula())
                .build();

        userRepository.save(user);
        return true;
    }

    public boolean login(AuthRequest authRequest) {
        if (!userRepository.existsByUsername(authRequest.getUsername()))
            throw new IllegalArgumentException("Incorrect Username or password");

        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow(null);

        if(!user.getPassword().equals(authRequest.getPassword()))
            throw new IllegalArgumentException("Incorrect Username or password");
        
        return true;
    }
}
