package com.juanfedevmaster.authbackendapi.services;

import org.springframework.stereotype.Service;

import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.AuthRequest;
import com.juanfedevmaster.authbackendapi.exceptions.InvalidCredentialsException;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import com.juanfedevmaster.authbackendapi.security.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean login(AuthRequest authRequest) {
        if (!userRepository.existsByUsername(authRequest.getUsername()))
            throw new InvalidCredentialsException("Incorrect Username or password");

        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow(() -> new InvalidCredentialsException("Incorrect Username or password"));

        if(!passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException("Incorrect Username or password");
        
        return true;
    }
}
