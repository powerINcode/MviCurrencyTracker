package com.example.feature_profile_impl

import com.example.core.rx.onMainThread
import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_api.data.model.Profile
import com.example.feature_profile_impl.ProfileScreenContract.ProfileIntent
import com.example.feature_profile_impl.ProfileScreenContract.ProfileState
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val reducer: ProfileReducer,
    private val profileRepository: ProfileRepository
) : com.example.core.domain.viewmodel.BaseViewModel<ProfileState>(reducer) {

    override fun doInit() {
        intentOf<ProfileIntent.SaveProfile>()
            .switchMapCompletable { intent ->
                profileRepository.saveProfile(
                    Profile(
                        first = intent.first,
                        second = intent.second,
                        last = intent.last
                    )
                )
                    .onMainThread()
                    .doOnComplete {
                        navigate(com.example.core.domain.routing.NavigationCommand.Finish)
                    }
            }

            .subscribeTillClear()



        profileRepository.getProfile()
            .subscribeTillClear { profile ->
                reducer.updateUserProfile( profile.first,
                         profile.second,
                         profile.last)

            }
    }
}