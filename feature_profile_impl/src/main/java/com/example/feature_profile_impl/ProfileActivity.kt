package com.example.feature_profile_impl

import android.os.Bundle
import androidx.activity.viewModels
import com.example.core.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.*

@AndroidEntryPoint
class ProfileActivity :
    BaseActivity<ProfileScreenContract.ProfileIntent, ProfileScreenContract.ProfileState, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileSaveButton.setOnClickListener {
            viewModel.send(
                ProfileScreenContract.ProfileIntent.SaveProfile(
                    first = profileFirstEditText.text.toString(),
                    second = profileLastEditText.text.toString(),
                    last = profileSecondEditText.text.toString()
                )
            )
        }
    }

    override fun render(state: ProfileScreenContract.ProfileState) {
        profileFirstEditText.setText(state.first)
        profileLastEditText.setText(state.last)
        profileSecondEditText.setText(state.second)
    }
}