package com.example.core.livedata

import androidx.lifecycle.Observer

class EventObserver<T>(private val block: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(t: Event<T>) {
        t.get()?.let(block)
    }
}