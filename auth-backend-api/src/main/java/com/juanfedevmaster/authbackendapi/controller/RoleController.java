package com.juanfedevmaster.authbackendapi.controller;

import com.juanfedevmaster.authbackendapi.entity.dto.RoleRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.RoleResponse;
import com.juanfedevmaster.authbackendapi.services.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role", description = "CRUD operations for roles")
public class RoleController {

    private final IRoleService roleService;

    @PostMapping
    @Operation(summary = "Create a role",
        responses = {
            @ApiResponse(responseCode = "201", description = "Role created",
                content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Role already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(request));
    }

    @GetMapping
    @Operation(summary = "Get all roles",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of roles"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<List<RoleResponse>> getAll() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a role by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Role found",
                content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<RoleResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a role by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Role updated",
                content = @Content(schema = @Schema(implementation = RoleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "409", description = "Role name already taken"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<RoleResponse> update(@PathVariable Integer id, @Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.updateRole(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role by id",
        responses = {
            @ApiResponse(responseCode = "204", description = "Role deleted"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
