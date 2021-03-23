package com.example.feature_profile_impl.declaration

import com.example.feature_profile_api.declaration.ProfileFeatureConfig
import com.example.feature_profile_impl.ProfileActivity
import javax.inject.Inject

class ProfileFeatureLauncher @Inject constructor(): com.example.core.domain.routing.FeatureLauncher {
    override fun suitFor(config: com.example.core.domain.routing.NavigationConfig): Boolean = config is ProfileFeatureConfig

    override fun launch(navigator: com.example.core.domain.routing.Navigator, config: com.example.core.domain.routing.NavigationConfig) {
        navigator.navigate(com.example.core.domain.routing.NavigationCommand.ActivityCommand(ProfileActivity::class.java))
    }
}