package com.example.core.livedata

import java.util.concurrent.atomic.AtomicBoolean

data class Event<T>(private val data: T?) {
    private val handled: AtomicBoolean = AtomicBoolean(false)

    fun get(): T? = if (handled.compareAndSet(false, true)) {
        data
    } else {
        null
    }
}