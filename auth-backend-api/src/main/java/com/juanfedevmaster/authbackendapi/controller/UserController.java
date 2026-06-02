package com.juanfedevmaster.authbackendapi.controller;

import com.juanfedevmaster.authbackendapi.entity.dto.AdminUserRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.AdminUserUpdateRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.ChangePasswordRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.SelfUpdateRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.UserResponse;
import com.juanfedevmaster.authbackendapi.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Admin user management and self-service profile endpoints")
public class UserController {

    private final IUserService userService;

    // ------------------------------------------------------ Self-service (/me)

    @GetMapping("/me")
    @Operation(summary = "Get the authenticated user's account")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser(authentication.getName()));
    }

    @PutMapping("/me")
    @Operation(summary = "Update the authenticated user's name, username and email")
    public ResponseEntity<UserResponse> updateMe(Authentication authentication,
                                                 @Valid @RequestBody SelfUpdateRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(authentication.getName(), request));
    }

    @PutMapping("/me/password")
    @Operation(summary = "Change the authenticated user's password")
    public ResponseEntity<Void> changePassword(Authentication authentication,
                                               @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    @Operation(summary = "Deactivate (delete) the authenticated user's own account")
    public ResponseEntity<Void> deleteMe(Authentication authentication) {
        userService.deleteCurrentUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    // ------------------------------------------------------------ Admin (CRUD)

    @GetMapping
    @Operation(summary = "List all users (admin)")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{cedula}")
    @Operation(summary = "Get a user by id (admin)")
    public ResponseEntity<UserResponse> getById(@PathVariable Long cedula) {
        return ResponseEntity.ok(userService.getByCedula(cedula));
    }

    @PostMapping
    @Operation(summary = "Create a user with a role (admin)")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody AdminUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PutMapping("/{cedula}")
    @Operation(summary = "Update a user's name, username, email and role (admin)")
    public ResponseEntity<UserResponse> update(@PathVariable Long cedula,
                                               @Valid @RequestBody AdminUserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(cedula, request));
    }

    @DeleteMapping("/{cedula}")
    @Operation(summary = "Delete a user (admin, self-delete blocked)")
    public ResponseEntity<Void> delete(@PathVariable Long cedula, Authentication authentication) {
        userService.deleteUser(cedula, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
