package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.Profile;
import com.juanfedevmaster.authbackendapi.entity.User;
import com.juanfedevmaster.authbackendapi.entity.dto.ProfileRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.ProfileResponse;
import com.juanfedevmaster.authbackendapi.exceptions.ProfileNotFoundException;
import com.juanfedevmaster.authbackendapi.repository.ProfileRepository;
import com.juanfedevmaster.authbackendapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        Profile profile = Profile.builder()
                .user(user)
                .phone(request.getPhone())
                .company(request.getCompany())
                .biography(request.getBiography())
                .avatarUrl(request.getAvatarUrl())
                .build();

        return toResponse(profileRepository.save(profile));
    }

    @Override
    public ProfileResponse getProfileById(Integer id) {
        return toResponse(profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id)));
    }

    @Override
    public ProfileResponse getProfileByUserId(Integer userId) {
        return toResponse(profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for user id: " + userId)));
    }

    @Override
    public ProfileResponse updateProfile(Integer id, ProfileRequest request) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));

        profile.setPhone(request.getPhone());
        profile.setCompany(request.getCompany());
        profile.setBiography(request.getBiography());
        profile.setAvatarUrl(request.getAvatarUrl());

        return toResponse(profileRepository.save(profile));
    }

    @Override
    public void deleteProfile(Integer id) {
        if (!profileRepository.existsById(id))
            throw new ProfileNotFoundException("Profile not found with id: " + id);
        profileRepository.deleteById(id);
    }

    private ProfileResponse toResponse(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getPhone(),
                profile.getCompany(),
                profile.getBiography(),
                profile.getAvatarUrl(),
                profile.getUpdated()
        );
    }
}
