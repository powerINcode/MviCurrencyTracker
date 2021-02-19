package com.example.feature_profile_impl

import com.example.core.viewmodel.StateReducer
import com.example.feature_profile_impl.ProfileScreenContract.*
import javax.inject.Inject

class ProfileStateReducer @Inject constructor() : StateReducer<ProfileState, ProfileChange> {
    override fun reduce(
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