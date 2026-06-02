package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.dto.AuthRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.AuthResponse;

public interface IAuthenticationService {
    AuthResponse login(AuthRequest authRequest);
}
