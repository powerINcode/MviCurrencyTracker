package com.example.core.di.modules

import com.example.core.routing.Navigator
import com.example.core.routing.NavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
interface BaseActivityModule {

    @ActivityScoped
    @Binds
    fun provideNavigator(navigator: NavigatorImpl): Navigator
}