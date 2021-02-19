package com.example.feature_profile_api.data

import com.example.feature_profile_api.data.model.Profile

interface ProfileRepository {
    suspend fun saveProfile(profile: Profile)
    suspend fun getProfile(): Profile?
}