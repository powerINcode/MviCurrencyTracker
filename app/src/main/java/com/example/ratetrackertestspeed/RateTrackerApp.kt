package com.example.ratetrackertestspeed

import android.app.Application
import com.example.core.application.ApiProvider
import com.example.core.domain.di.CoreComponent
import com.example.core.domain.di.CoreComponentProvider
import com.example.core.network.di.CoreNetworkApiProvider
import com.example.core.storage.di.CoreStorageComponent
import com.example.core.storage.di.CoreStorageProvider
import com.example.feature_profile_impl.di.ProfileFeatureComponent
import com.example.feature_profile_impl.di.ProfileFeatureComponentProvider
import com.example.feature_rate_tracker_impl.di.RateTrackerFeatureComponent
import com.example.feature_rate_tracker_impl.di.RateTrackerFeatureComponentProvider
import com.example.ratetrackertestspeed.di.ApplicationComponent
import com.example.ratetrackertestspeed.di.DaggerApplicationComponent

class RateTrackerApp : Application(), ApiProvider {

    private lateinit var applicationComponent: ApplicationComponent

    private val apis: MutableMap<Class<*>, Any> = mutableMapOf()

    override fun <T : Any> getApi(api: Class<T>): T {
        return apis[api] as T
    }


    override fun onCreate() {
        applicationComponent = DaggerApplicationComponent.factory().create()

        super.onCreate()

        beforeInit()
        featureInit()
    }

    private fun beforeInit() {
        CoreComponentProvider.init(CoreComponent.Args(applicationComponent))
        CoreStorageProvider.init(CoreStorageComponent.Args(context = this)).addToApi()
        CoreNetworkApiProvider.init(Unit).addToApi()
    }

    private fun featureInit() {
        RateTrackerFeatureComponentProvider.init(
            RateTrackerFeatureComponent.Args(
                coreNetworkApi = CoreNetworkApiProvider.instance,
                coreStorageApi = CoreStorageProvider.instance
            )
        ).addToApi()

        ProfileFeatureComponentProvider.init(ProfileFeatureComponent.Args(coreStorageApi = CoreStorageProvider.instance)).addToApi()
    }

    private inline fun <reified T : Any> T.addToApi() = apis.put(T::class.java, this)
}