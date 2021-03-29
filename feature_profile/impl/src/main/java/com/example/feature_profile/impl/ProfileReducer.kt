package com.example.feature_profile.impl

import com.example.core.domain.viewmodel.StateReducer
import com.example.feature_profile.impl.ProfileScreenContract.ProfileState
import javax.inject.Inject

class ProfileReducer @Inject constructor(): StateReducer<ProfileState>(ProfileState.EMPTY) {
    fun updateUserProfile(first: String, second: String, last: String) {
        state.copy(first = first, second = second, last = last).commit()
    }
}