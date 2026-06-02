package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.Role;
import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.AdminUserRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.AdminUserUpdateRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.ChangePasswordRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.SelfUpdateRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.UserResponse;
import com.juanfedevmaster.authbackendapi.exceptions.EmailAlreadyExistsException;
import com.juanfedevmaster.authbackendapi.exceptions.InvalidCredentialsException;
import com.juanfedevmaster.authbackendapi.exceptions.RoleNotFoundException;
import com.juanfedevmaster.authbackendapi.exceptions.UserNotFoundException;
import com.juanfedevmaster.authbackendapi.repository.RoleRepository;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import com.juanfedevmaster.authbackendapi.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ------------------------------------------------------------------ Admin

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getByCedula(Long cedula) {
        return UserResponse.from(findUserByCedula(cedula));
    }

    @Override
    public UserResponse createUser(AdminUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        if (userRepository.existsByUsername(request.getUsername()))
            throw new EmailAlreadyExistsException("Username already exists: " + request.getUsername());
        if (userRepository.existsByCedula(request.getCedula()))
            throw new EmailAlreadyExistsException("ID card already registered: " + request.getCedula());

        Role role = findRoleByName(request.getRole());

        User user = User.builder()
                .cedula(request.getCedula())
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(true)
                .build();

        return UserResponse.from(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long cedula, AdminUserUpdateRequest request) {
        User user = findUserByCedula(cedula);

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername()))
            throw new EmailAlreadyExistsException("Username already exists: " + request.getUsername());

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(findRoleByName(request.getRole()));
        if (request.getEnabled() != null)
            user.setEnabled(request.getEnabled());

        return UserResponse.from(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long cedula, String requesterEmail) {
        User user = findUserByCedula(cedula);
        if (user.getEmail().equals(requesterEmail))
            throw new IllegalArgumentException("You cannot delete your own account from the admin panel");
        userRepository.delete(user);
    }

    // ----------------------------------------------------------- Self-service

    @Override
    public UserResponse getCurrentUser(String email) {
        return UserResponse.from(findUserByEmail(email));
    }

    @Override
    public UserResponse updateCurrentUser(String email, SelfUpdateRequest request) {
        User user = findUserByEmail(email);

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        if (!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername()))
            throw new EmailAlreadyExistsException("Username already exists: " + request.getUsername());

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        return UserResponse.from(userRepository.save(user));
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findUserByEmail(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new InvalidCredentialsException("Current password is incorrect");
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteCurrentUser(String email) {
        userRepository.delete(findUserByEmail(email));
    }

    // ---------------------------------------------------------------- Helpers

    private User findUserByCedula(Long cedula) {
        return userRepository.findById(cedula)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + cedula));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    private Role findRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + name));
    }
}
