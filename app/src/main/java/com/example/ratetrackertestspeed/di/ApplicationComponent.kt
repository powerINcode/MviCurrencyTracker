package com.example.ratetrackertestspeed.di

import com.example.core.domain.routing.FeatureLaunchersProvider
import dagger.Component

@Component(
    modules = [
        ApplicationModule::class
    ]
)
interface ApplicationComponent: FeatureLaunchersProvider {

    @Component.Factory
    interface Factory {
        fun create(): ApplicationComponent
    }

}