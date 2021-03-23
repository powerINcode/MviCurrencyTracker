package com.example.core.domain.di

import com.example.core.di.components.ComponentProvider
import com.example.core.domain.routing.FeatureLaunchersProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CoreModule::class
    ]
)
interface CoreComponent: FeatureLaunchersProvider {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance featureLaunchersProvider: FeatureLaunchersProvider): CoreComponent
    }

    data class Args(val featureLaunchersProvider: FeatureLaunchersProvider)
}

object CoreComponentProvider: ComponentProvider<CoreComponent.Args, CoreComponent>({ args ->
    DaggerCoreComponent.factory().create(featureLaunchersProvider = args.featureLaunchersProvider)
})