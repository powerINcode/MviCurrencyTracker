package com.example.feature_profile_impl.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.core.di.keys.ViewModelKey
import com.example.core.di.scopes.ActivityScope
import com.example.core.routing.Navigator
import com.example.core.routing.NavigatorFactory
import com.example.feature_profile_impl.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal interface ProfileActivityModule {
    @ActivityScope
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    @Binds
    fun bindsProfileViewModel(vm: ProfileViewModel): ViewModel
}