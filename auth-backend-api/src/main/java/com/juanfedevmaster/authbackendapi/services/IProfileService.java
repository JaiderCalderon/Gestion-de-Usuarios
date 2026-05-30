package com.juanfedevmaster.authbackendapi.services;

import com.juanfedevmaster.authbackendapi.entity.dto.ProfileRequest;
import com.juanfedevmaster.authbackendapi.entity.dto.ProfileResponse;

public interface IProfileService {
    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getProfileById(Integer id);
    ProfileResponse getProfileByUserId(Integer userId);
    ProfileResponse updateProfile(Integer id, ProfileRequest request);
    void deleteProfile(Integer id);
}
