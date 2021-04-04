package com.example.feature_profile.impl

import com.example.core.domain.routing.Finish
import com.example.core.domain.routing.Navigator
import com.example.core.ui.presenter.BasePresenter
import com.example.feature_profile.api.data.model.Profile
import com.example.feature_profile.impl.ProfileScreenContract.ProfileIntent
import com.example.feature_profile.impl.ProfileScreenContract.ProfileState
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    private val navigator: Navigator
): BasePresenter<ProfileState, ProfileReducer, ProfileViewModel>() {

    override fun onCreated() {
        super.onCreated()

        intentOf<ProfileIntent.SaveProfile>()
            .switchMapCompletable { intent ->
                viewModel.saveProfile(
                    Profile(
                        first = intent.first,
                        second = intent.second,
                        last = intent.last
                    )
                )
                    .doOnComplete { navigator.navigate(Finish) }
            }
            .subscribeTillAttach()
    }
}