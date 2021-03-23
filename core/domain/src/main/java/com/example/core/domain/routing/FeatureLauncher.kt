package com.example.core.domain.routing

interface FeatureLauncher {
    fun suitFor(config: NavigationConfig): Boolean
    fun launch(navigator: Navigator, config: NavigationConfig)
}