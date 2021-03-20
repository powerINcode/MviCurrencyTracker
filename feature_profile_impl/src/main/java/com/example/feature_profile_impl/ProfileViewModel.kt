package com.example.feature_profile_impl

import com.example.core.routing.NavigationCommand
import com.example.core.viewmodel.BaseViewModel
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
) : BaseViewModel<ProfileState>(reducer) {

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
                navigate(NavigationCommand.Finish)
            }

        profileRepository.getProfile()?.let { profile ->
            reducer.updateUserProfile(profile.first, profile.second, profile.last)
        }
    }
}