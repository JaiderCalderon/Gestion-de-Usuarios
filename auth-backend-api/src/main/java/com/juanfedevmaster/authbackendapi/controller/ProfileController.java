package com.juanfedevmaster.authbackendapi.controller;

import com.juanfedevmaster.authbackendapi.entity.dto.ProfileRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.ProfileResponse;
import com.juanfedevmaster.authbackendapi.services.IProfileService;
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

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "CRUD operations for user profiles")
public class ProfileController {

    private final IProfileService profileService;

    @PostMapping
    @Operation(summary = "Create a user profile",
        responses = {
            @ApiResponse(responseCode = "201", description = "Profile created",
                content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createProfile(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a profile by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile found",
                content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<ProfileResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get a profile by user id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile found",
                content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<ProfileResponse> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a profile by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile updated",
                content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<ProfileResponse> update(@PathVariable Integer id, @Valid @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a profile by id",
        responses = {
            @ApiResponse(responseCode = "204", description = "Profile deleted"),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
