package com.example.feature_profile_impl

import com.example.core.routing.NavigationCommand
import com.example.core.viewmodel.BaseViewModel
import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_api.data.model.Profile
import com.example.feature_profile_impl.ProfileScreenContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    reducer: ProfileStateReducer,
    private val profileRepository: ProfileRepository
) : BaseViewModel<ProfileIntent, ProfileState, ProfileChange>(reducer) {
    override fun getInitialState(): ProfileState = ProfileState.EMPTY

    override fun getInitialChange(): ProfileChange = ProfileChange.DoNothing

    override suspend fun doInit() {
        intentSharedFlow.filterIsInstance<ProfileIntent.SaveProfile>()
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
            onChange(
                ProfileChange.UpdateUser(
                    first = profile.first,
                    second = profile.second,
                    last = profile.last
                )
            )
        }
    }
}