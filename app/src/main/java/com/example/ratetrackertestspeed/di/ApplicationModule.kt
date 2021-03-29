package com.example.ratetrackertestspeed.di

import com.example.core.domain.routing.FeatureLauncher
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface ApplicationModule {

    @Binds
    @IntoSet
    fun bindsProfileFeatureLauncher(launcher: com.example.feature_profile.impl.declaration.ProfileFeatureLauncher): FeatureLauncher
}