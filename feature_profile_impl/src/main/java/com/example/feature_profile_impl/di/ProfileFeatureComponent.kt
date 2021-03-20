package com.example.feature_profile_impl.di

import com.example.core.di.components.ComponentProvider
import com.example.core.di.scopes.FeatureScope
import com.example.core.storage.di.CoreStorageApi
import com.example.feature_profile_api.ProfileApi
import dagger.Component

@FeatureScope
@Component(
    modules = [
        ProfileFeatureModule::class
    ],
    dependencies = [
        CoreStorageApi::class
    ]
)
interface ProfileFeatureComponent: ProfileApi {

    fun getProfileActivityBuilder(): ProfileActivityComponent.Builder

    @Component.Factory
    interface Factory {
        fun create(
            coreStorageApi: CoreStorageApi
        ): ProfileFeatureComponent
    }

    data class Args(
        val coreStorageApi: CoreStorageApi
    )
}

object ProfileFeatureComponentProvider : ComponentProvider<ProfileFeatureComponent.Args, ProfileFeatureComponent>({ args ->
    DaggerProfileFeatureComponent.factory().create(
        coreStorageApi = args.coreStorageApi
    )
})