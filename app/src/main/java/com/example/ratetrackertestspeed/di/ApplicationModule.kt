package com.example.ratetrackertestspeed.di

import com.example.core.routing.FeatureLauncher
import com.example.feature_profile_impl.declaration.ProfileFeatureLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationModule {

    @Binds
    @IntoSet
    fun bindsProfileFeatureLauncher(launcher: ProfileFeatureLauncher): FeatureLauncher
}