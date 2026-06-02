package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.dto.RoleRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.RoleResponse;
import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleRequest request);
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(Integer id);
    RoleResponse updateRole(Integer id, RoleRequest request);
    void deleteRole(Integer id);
}
