package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;

public interface IRegistrationService {
    boolean register(RegisterUserRequest userToRegister);
}
