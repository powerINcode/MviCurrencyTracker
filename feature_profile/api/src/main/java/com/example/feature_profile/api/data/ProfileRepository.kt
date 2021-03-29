package com.example.feature_profile.api.data

import com.example.feature_profile.api.data.model.Profile

interface ProfileRepository {
    suspend fun saveProfile(profile: Profile)
    suspend fun getProfile(): Profile?
}