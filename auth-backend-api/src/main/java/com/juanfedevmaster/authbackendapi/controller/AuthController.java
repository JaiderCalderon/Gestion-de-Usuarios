package com.juanfedevmaster.authbackendapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juanfedevmaster.authbackendapi.entity.dto.AuthRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.AuthResponse;
import com.juanfedevmaster.authbackendapi.entity.dto.RegisterUserRequest;
import com.juanfedevmaster.authbackendapi.services.IAuthService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register and login endpoints — no token required")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
        description = "Action to register a new user into the aplication.",
        responses = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode =  "500", description = "Internal server Error.")
        }   
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterUserRequest userToRegister) {
        boolean created = authService.register(userToRegister);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(created, "User created"));
    }

    @PostMapping("/login")
    @Operation(summary = "login user with password",
        description = "Action to login a user into the aplication.",
        responses = {
            @ApiResponse(responseCode = "200", description = "User authenticated", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode =  "500", description = "Internal server Error.")
        }   
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        boolean ok = authService.login(authRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(ok, "Authenticated"));
    }
    
}
