package com.example.core_network_impl.di

import com.example.core.di.components.ComponentProvider
import com.example.core_network_api.CoreNetworkApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CoreNetworkModule::class
    ]
)
interface CoreNetworkComponent: CoreNetworkApi {

    @Component.Factory
    interface Factory {
        fun create(): CoreNetworkComponent
    }
}

object CoreNetworkApiProvider: ComponentProvider<Unit, CoreNetworkApi>({ args ->
    DaggerCoreNetworkComponent.factory().create()
})