package com.example.feature_rate_tracker_impl.di

import androidx.appcompat.app.AppCompatActivity
import com.example.core.di.builders.BaseActivityBuilder
import com.example.core.di.components.BaseActivityComponent
import com.example.core.di.modules.BaseActivityModule
import com.example.core.di.scopes.ActivityScope
import com.example.feature_rate_tracker_impl.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        BaseActivityModule::class,
        RateTrackerActivityModule::class
    ]
)
interface RateTrackerActivityComponent: BaseActivityComponent<MainActivity> {

    @Subcomponent.Builder
    interface Builder: BaseActivityBuilder<RateTrackerActivityComponent, AppCompatActivity, Builder>
}