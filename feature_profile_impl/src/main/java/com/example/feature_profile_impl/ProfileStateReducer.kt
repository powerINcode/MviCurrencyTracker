package com.example.feature_profile_impl

import com.example.core.domain.viewmodel.StateReducer
import com.example.feature_profile_impl.ProfileScreenContract.ProfileState
import javax.inject.Inject

class ProfileStateReducer @Inject constructor() : StateReducer<ProfileState>(ProfileState.EMPTY) {
    fun updateUserProfile(first: String, second: String, last: String) {
        state.copy(first = first, second = second, last = last).commit()
    }
}