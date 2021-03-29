package com.example.feature_profile.impl

import android.os.Bundle
import androidx.activity.viewModels
import com.example.core.ui.activity.BaseActivity
import com.example.core.ui.viewbinding.viewBindings
import com.example.feature_profile.impl.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity :
    BaseActivity<ProfileScreenContract.ProfileState, ProfileViewModel, ActivityProfileBinding>() {

    override val viewBinding: ActivityProfileBinding by viewBindings(ActivityProfileBinding::inflate)

    override val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewBinding) {
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
    }

    override fun render(state: ProfileScreenContract.ProfileState) {
        with(viewBinding) {
            profileFirstEditText.setText(state.first)
            profileLastEditText.setText(state.last)
            profileSecondEditText.setText(state.second)
        }
    }
}