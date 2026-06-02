package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.dto.AdminUserRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.AdminUserUpdateRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.ChangePasswordRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.SelfUpdateRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.UserResponse;

import java.util.List;

public interface IUserService {

    // Admin operations
    List<UserResponse> getAllUsers();
    UserResponse getByCedula(Long cedula);
    UserResponse createUser(AdminUserRequest request);
    UserResponse updateUser(Long cedula, AdminUserUpdateRequest request);
    void deleteUser(Long cedula, String requesterEmail);

    // Self-service operations (principal = email)
    UserResponse getCurrentUser(String email);
    UserResponse updateCurrentUser(String email, SelfUpdateRequest request);
    void changePassword(String email, ChangePasswordRequest request);
    void deleteCurrentUser(String email);
}
