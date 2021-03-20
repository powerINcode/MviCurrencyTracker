package com.example.feature_profile_impl

import com.example.core.routing.NavigationCommand
import com.example.core.rx.onMainThread
import com.example.core.viewmodel.BaseViewModel
import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_api.data.model.Profile
import com.example.feature_profile_impl.ProfileScreenContract.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val reducer: ProfileReducer,
    private val profileRepository: ProfileRepository
) : BaseViewModel<ProfileState>(reducer) {

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
                        navigate(NavigationCommand.Finish)
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