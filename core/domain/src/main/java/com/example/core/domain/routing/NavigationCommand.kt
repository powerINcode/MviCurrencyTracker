package com.example.core.domain.routing

import androidx.appcompat.app.AppCompatActivity

sealed class NavigationCommand

data class FeatureCommand(val config: NavigationConfig) : NavigationCommand()
data class ActivityCommand<T : AppCompatActivity>(val destination: Class<T>) : NavigationCommand()
object Finish : NavigationCommand()
