package com.example.feature_profile.impl

import com.example.core.domain.viewmodel.BaseViewModel
import com.example.core.rx.onMainThread
import com.example.feature_profile.api.data.ProfileRepository
import com.example.feature_profile.api.data.model.Profile
import com.example.feature_profile.impl.ProfileScreenContract.ProfileState
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    reducer: ProfileReducer,
    private val profileRepository: ProfileRepository
) : BaseViewModel<ProfileState, ProfileReducer>(reducer) {

    override fun doInit() {
        profileRepository.getProfile()
            .subscribeTillClear { profile ->
                reducer.updateUserProfile( profile.first,
                         profile.second,
                         profile.last)

            }
    }

     fun saveProfile(profile: Profile): Completable = profileRepository.saveProfile(profile)
}