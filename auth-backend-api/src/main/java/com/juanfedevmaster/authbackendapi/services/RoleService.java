package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.Role;
import com.juanfedevmaster.authbackendapi.entity.dto.RoleRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.RoleResponse;
import com.juanfedevmaster.authbackendapi.exceptions.RoleAlreadyExistsException;
import com.juanfedevmaster.authbackendapi.exceptions.RoleNotFoundException;
import com.juanfedevmaster.authbackendapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.getName()))
            throw new RoleAlreadyExistsException("Role already exists: " + request.getName());

        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return toResponse(roleRepository.save(role));
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRoleById(Integer id) {
        return toResponse(roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id)));
    }

    @Override
    public RoleResponse updateRole(Integer id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));

        if (!role.getName().equals(request.getName()) && roleRepository.existsByName(request.getName()))
            throw new RoleAlreadyExistsException("Role already exists: " + request.getName());

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        return toResponse(roleRepository.save(role));
    }

    @Override
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id))
            throw new RoleNotFoundException("Role not found with id: " + id);
        roleRepository.deleteById(id);
    }

    private RoleResponse toResponse(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(), role.getCreated());
    }
}
