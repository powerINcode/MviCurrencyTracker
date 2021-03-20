package com.example.feature_profile_impl

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
}