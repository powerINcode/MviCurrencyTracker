package com.example.core.routing

interface FeatureLauncher {
    fun suitFor(config: NavigationConfig): Boolean
    fun launch(navigator: Navigator, config: NavigationConfig)
}