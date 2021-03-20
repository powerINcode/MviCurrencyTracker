package com.example.feature_profile_impl.di

import androidx.appcompat.app.AppCompatActivity
import com.example.core.di.builders.BaseActivityBuilder
import com.example.core.di.components.BaseActivityComponent
import com.example.core.di.scopes.ActivityScope
import com.example.core.network.di.modules.BaseActivityModule
import com.example.feature_profile_impl.ProfileActivity
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