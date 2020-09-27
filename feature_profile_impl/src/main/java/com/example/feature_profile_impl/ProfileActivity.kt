package com.example.feature_profile_impl

import android.os.Bundle
import com.example.core.activity.BaseActivity
import com.example.feature_profile_impl.di.ProfileActivityComponent
import com.example.feature_profile_impl.di.ProfileFeatureComponent
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity :
    BaseActivity<ProfileActivityComponent, ProfileScreenContract.ProfileIntent, ProfileScreenContract.ProfileState, ProfileViewModel>() {
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