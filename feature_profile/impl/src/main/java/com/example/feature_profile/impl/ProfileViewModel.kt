package com.example.feature_profile.impl

import com.example.core.domain.viewmodel.BaseViewModel
import com.example.feature_profile.api.data.ProfileRepository
import com.example.feature_profile.api.data.model.Profile
import com.example.feature_profile.impl.ProfileScreenContract.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    reducer: ProfileStateReducer,
    private val profileRepository: ProfileRepository
) : BaseViewModel<ProfileState, ProfileStateReducer>(reducer) {

    override suspend fun doInit() {


        profileRepository.getProfile()?.let { profile ->
            reducer.updateUserProfile(profile.first, profile.second, profile.last)
        }
    }

    suspend fun saveProfile(profile: Profile) {
        profileRepository.saveProfile(profile)
    }
}