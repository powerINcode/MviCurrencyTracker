package com.example.feature_profile_impl

import com.example.core.routing.NavigationCommand
import com.example.core.rx.onMainThread
import com.example.core.viewmodel.BaseViewModel
import com.example.core_storage.models.profile.ProfileEntity
import com.example.feature_profile_api.data.ProfileRepository
import com.example.feature_profile_api.data.model.Profile
import com.example.feature_profile_impl.ProfileScreenContract.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseViewModel<ProfileIntent, ProfileState, ProfileChange>() {
    override fun getInitialState(): ProfileState = ProfileState.EMPTY

    override fun getInitialChange(): ProfileChange = ProfileChange.DoNothing

    init {
        intentSubject.ofType(ProfileIntent.SaveProfile::class.java)
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
                onChange(
                    ProfileChange.UpdateUser(
                        first = profile.first,
                        second = profile.second,
                        last = profile.last
                    )
                )
            }
    }

    override fun reducer(
        state: ProfileState,
        change: ProfileChange
    ): ProfileState {
        return when (change) {
            is ProfileChange.UpdateUser -> state.copy(
                first = change.first,
                second = change.second,
                last = change.last
            )
            is ProfileChange.DoNothing -> state
        }
    }
}