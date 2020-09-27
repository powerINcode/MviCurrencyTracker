package com.example.core.livedata

import androidx.lifecycle.MutableLiveData

class MutableLiveEvent<T> : LiveEvent<T>() {
    var event: T? = null
        get() = value?.get()
        set(eventData) {
            field = eventData
            value = Event(eventData)
        }
}