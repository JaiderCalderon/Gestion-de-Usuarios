package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.AuthRequest;
import com.juanfedevmaster.authbackendapi.exceptions.InvalidCredentialsException;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import com.juanfedevmaster.authbackendapi.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Incorrect email or password"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPasswordHash()))
            throw new InvalidCredentialsException("Incorrect email or password");

        return true;
    }
}
