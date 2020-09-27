package com.example.core_storage.di

import android.content.Context
import com.example.core.di.components.ComponentProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CoreStorageModule::class
    ]
)
interface CoreStorageComponent : CoreStorageApi {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreStorageComponent
    }

    data class Args(
        val context: Context
    )
}

object CoreStorageProvider : ComponentProvider<CoreStorageComponent.Args, CoreStorageComponent>({ args ->
    DaggerCoreStorageComponent.factory().create(
        context = args.context
    )
})