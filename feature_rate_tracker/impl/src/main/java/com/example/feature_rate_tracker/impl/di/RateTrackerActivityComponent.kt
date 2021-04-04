package com.example.feature_rate_tracker.impl.di

import androidx.appcompat.app.AppCompatActivity
import com.example.core.domain.di.builders.BaseActivityBuilder
import com.example.core.di.components.BaseActivityComponent
import com.example.core.di.scopes.ActivityScope
import com.example.core.ui.activity.di.BaseActivityModule
import com.example.feature_rate_tracker.impl.MainActivity
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