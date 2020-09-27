package com.example.feature_profile_impl

import com.example.core.mvi.Change

interface ProfileScreenContract {
    data class ProfileState(
        val first: String,
        val second: String,
        val last: String
    ) {
        companion object {
            val EMPTY = ProfileState(
                first = "",
                second = "",
                last = ""
            )
        }
    }

    sealed class ProfileIntent {
        data class SaveProfile(
            val first: String,
            val second: String,
            val last: String
        ): ProfileIntent()
    }

    sealed class ProfileChange : Change {
        object DoNothing: ProfileChange()
        data class UpdateUser(
            val first: String,
            val second: String,
            val last: String
        ): ProfileChange()
    }
}