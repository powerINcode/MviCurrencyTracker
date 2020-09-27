package com.example.feature_profile_impl.declaration

import com.example.core.routing.FeatureLauncher
import com.example.core.routing.NavigationCommand
import com.example.core.routing.NavigationConfig
import com.example.core.routing.Navigator
import com.example.feature_profile_api.declaration.ProfileFeatureConfig
import com.example.feature_profile_impl.ProfileActivity
import javax.inject.Inject

class ProfileFeatureLauncher @Inject constructor(): FeatureLauncher {
    override fun suitFor(config: NavigationConfig): Boolean = config is ProfileFeatureConfig

    override fun launch(navigator: Navigator, config: NavigationConfig) {
        navigator.navigate(NavigationCommand.ActivityCommand(ProfileActivity::class.java))
    }
}