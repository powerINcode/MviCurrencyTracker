package com.example.core.domain.di

import com.example.core.domain.routing.FeatureLauncher
import com.example.core.domain.routing.FeatureLaunchersProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface CoreModule {

    companion object {
        @Singleton
        @Provides
        fun provideFeatureLaunchers(featureLaunchersProvider: FeatureLaunchersProvider): Set<FeatureLauncher> =
            featureLaunchersProvider.getFeatureLaunchers()
    }
}