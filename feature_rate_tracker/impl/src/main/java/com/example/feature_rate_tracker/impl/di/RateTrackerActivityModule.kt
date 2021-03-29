package com.example.feature_rate_tracker.impl.di

import androidx.lifecycle.ViewModel
import com.example.core.di.keys.ViewModelKey
import com.example.core.di.scopes.ActivityScope
import com.example.feature_rate_tracker.impl.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface RateTrackerActivityModule {
    @ActivityScope
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindsMainViewModel(vm: MainViewModel): ViewModel

}