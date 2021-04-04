package com.example.feature_profile_impl.di

import androidx.appcompat.app.AppCompatActivity
import com.example.core.domain.di.builders.BaseActivityBuilder
import com.example.core.di.components.BaseActivityComponent
import com.example.core.di.scopes.ActivityScope
import com.example.core.ui.activity.di.BaseActivityModule
import com.example.feature_profile.impl.ProfileActivity
import com.example.feature_profile.impl.di.ProfileActivityModule
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        BaseActivityModule::class,
        ProfileActivityModule::class
    ]
)
interface ProfileActivityComponent: BaseActivityComponent<ProfileActivity> {

    @Subcomponent.Builder
    interface Builder: BaseActivityBuilder<ProfileActivityComponent, AppCompatActivity, Builder>
}