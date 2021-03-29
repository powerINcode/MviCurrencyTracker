package com.example.feature_profile.impl

import com.example.core.domain.viewmodel.BaseViewModel
import com.example.core.rx.onMainThread
import com.example.feature_profile.api.data.ProfileRepository
import com.example.feature_profile.api.data.model.Profile
import com.example.feature_profile.impl.ProfileScreenContract.ProfileIntent
import com.example.feature_profile.impl.ProfileScreenContract.ProfileState
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