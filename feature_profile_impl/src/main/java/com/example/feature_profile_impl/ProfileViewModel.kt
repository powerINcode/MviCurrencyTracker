package com.example.feature_profile_impl

import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_api.data.model.Profile
import com.example.feature_profile_impl.ProfileScreenContract.ProfileIntent
import com.example.feature_profile_impl.ProfileScreenContract.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val reducer: ProfileStateReducer,
    private val profileRepository: ProfileRepository
) : com.example.core.domain.viewmodel.BaseViewModel<ProfileState>(reducer) {

    override suspend fun doInit() {
        intentOf<ProfileIntent.SaveProfile>()
            .mapLatest { intent ->
                profileRepository.saveProfile(
                    Profile(
                        first = intent.first,
                        second = intent.second,
                        last = intent.last
                    )
                )

            }
            .collectInScope {
                navigate(com.example.core.domain.routing.NavigationCommand.Finish)
            }

        profileRepository.getProfile()?.let { profile ->
            reducer.updateUserProfile(profile.first, profile.second, profile.last)
        }
    }
}