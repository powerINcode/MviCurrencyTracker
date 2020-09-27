package com.example.core.routing

interface FeatureLaunchersProvider {
    fun getFeatureLaunchers(): Set<FeatureLauncher>
}