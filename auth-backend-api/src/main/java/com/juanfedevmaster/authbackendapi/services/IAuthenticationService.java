package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.dto.AuthRequest;

public interface IAuthenticationService {
    boolean login(AuthRequest authRequest);
    
}
