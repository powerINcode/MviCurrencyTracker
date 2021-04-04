package com.example.feature_profile.impl

import com.example.core.domain.routing.Finish
import com.example.core.domain.routing.Navigator
import com.example.core.ui.presenter.BasePresenter
import com.example.feature_profile.api.data.model.Profile
import com.example.feature_profile.impl.ProfileScreenContract.ProfileIntent
import com.example.feature_profile.impl.ProfileScreenContract.ProfileState
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    private val navigator: Navigator
): BasePresenter<ProfileState, ProfileStateReducer, ProfileViewModel>() {

    override fun onCreated() {
        super.onCreated()

        intentOf<ProfileIntent.SaveProfile>()
            .mapLatest { intent ->
                viewModel.saveProfile(
                    Profile(
                        first = intent.first,
                        second = intent.second,
                        last = intent.last
                    )
                )

            }
            .collectInScope {
                navigator.navigate(Finish)
            }
    }
}