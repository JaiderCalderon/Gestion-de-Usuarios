package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.dto.AuthResponse;
import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;

public interface IRegistrationService {
    AuthResponse register(RegisterUserRequest userToRegister);
}
