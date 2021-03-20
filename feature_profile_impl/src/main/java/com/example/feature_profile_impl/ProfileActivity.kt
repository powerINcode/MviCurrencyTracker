package com.example.feature_profile_impl

import android.os.Bundle
import com.example.core.activity.BaseActivity
import com.example.core.activity.viewbinding.viewBindings
import com.example.feature_profile_impl.databinding.ActivityProfileBinding
import com.example.feature_profile_impl.di.ProfileActivityComponent
import com.example.feature_profile_impl.di.ProfileFeatureComponent

class ProfileActivity :
    BaseActivity<ProfileActivityComponent, ProfileScreenContract.ProfileState, ProfileViewModel, ActivityProfileBinding>() {

    override val viewBinding: ActivityProfileBinding by viewBindings(ActivityProfileBinding::inflate)

    override fun getViewModelClass(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun createComponent(): ProfileActivityComponent =
        provideApi(ProfileFeatureComponent::class.java)
            .getProfileActivityBuilder()
            .activity(this)
            .build()

    override fun inject(component: ProfileActivityComponent) {
        component.inject(this)
    }

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