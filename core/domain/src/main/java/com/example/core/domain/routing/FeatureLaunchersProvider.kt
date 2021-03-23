package com.example.core.domain.routing

interface FeatureLaunchersProvider {
    fun getFeatureLaunchers(): Set<FeatureLauncher>
}