package com.example.feature_profile.impl.declaration

import com.example.core.domain.routing.ActivityCommand
import com.example.core.domain.routing.NavigationConfig
import com.example.core.domain.routing.Navigator
import com.example.feature_profile.api.declaration.ProfileFeatureConfig
import com.example.feature_profile.impl.ProfileActivity
import javax.inject.Inject

class ProfileFeatureLauncher @Inject constructor(): com.example.core.domain.routing.FeatureLauncher {
    override fun suitFor(config: NavigationConfig): Boolean = config is ProfileFeatureConfig

    override fun launch(navigator: Navigator, config: NavigationConfig) {
        navigator.navigate(ActivityCommand(ProfileActivity::class.java))
    }
}