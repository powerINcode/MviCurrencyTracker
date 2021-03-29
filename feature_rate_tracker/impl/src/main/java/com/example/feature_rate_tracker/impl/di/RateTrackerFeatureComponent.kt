package com.example.feature_rate_tracker.impl.di

import com.example.core.di.components.ComponentProvider
import com.example.core.di.scopes.FeatureScope
import com.example.core.network.di.CoreNetworkApi
import com.example.core.storage.di.CoreStorageApi
import com.example.feature_rate_tracker_api.RateTrackerApi
import dagger.Component

@FeatureScope
@Component(
    modules = [
        RateTrackerFeatureModule::class
    ],
    dependencies = [
        CoreNetworkApi::class,
        CoreStorageApi::class
    ]
)
interface RateTrackerFeatureComponent: RateTrackerApi {

    fun getRateTrackerActivityBuilder(): RateTrackerActivityComponent.Builder

    @Component.Factory
    interface Factory {
        fun create(
            coreNetworkApi: CoreNetworkApi,
            coreStorageApi: CoreStorageApi
        ): RateTrackerFeatureComponent
    }

    data class Args(
        val coreNetworkApi: CoreNetworkApi,
        val coreStorageApi: CoreStorageApi
    )
}

object RateTrackerFeatureComponentProvider : ComponentProvider<RateTrackerFeatureComponent.Args, RateTrackerFeatureComponent>({ args ->
    DaggerRateTrackerFeatureComponent.factory().create(
        coreNetworkApi = args.coreNetworkApi,
        coreStorageApi = args.coreStorageApi
    )
})