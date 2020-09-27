package com.example.core.di.components

import androidx.annotation.MainThread

open class ComponentProvider<Args, Component: Any>(private val block: (Args) -> Component) {
    private lateinit var component: Component

    val instance: Component get() = component

    @MainThread
    fun init(args: Args): Component {
        if (!this::component.isInitialized) {
            component = block(args)
        }

        return component
    }
}