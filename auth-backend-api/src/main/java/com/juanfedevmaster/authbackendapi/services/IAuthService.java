package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.dto.AuthRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;

public interface IAuthService {
    boolean register(RegisterUserRequest userToRegister);
    boolean login(AuthRequest authRequest);
}
