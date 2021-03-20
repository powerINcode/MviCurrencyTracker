package com.example.feature_profile_api.data

import com.example.feature_profile_api.data.model.Profile
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface ProfileRepository {
    fun saveProfile(profile: Profile): Completable
    fun getProfile(): Maybe<Profile>
}