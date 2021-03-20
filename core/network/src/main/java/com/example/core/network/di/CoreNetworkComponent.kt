package com.example.core.network.di

import com.example.core.di.components.ComponentProvider
import dagger.Component
import retrofit2.Retrofit
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

interface CoreNetworkApi {
    fun getRetrofit(): Retrofit
}

object CoreNetworkApiProvider: ComponentProvider<Unit, CoreNetworkApi>({ args ->
    DaggerCoreNetworkComponent.factory().create()
})