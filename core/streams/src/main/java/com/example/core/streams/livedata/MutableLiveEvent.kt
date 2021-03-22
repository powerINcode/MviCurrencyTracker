package com.example.core.streams.livedata

class MutableLiveEvent<T> : LiveEvent<T>() {
    var event: T? = null
        get() = value?.get()
        set(eventData) {
            field = eventData
            value = Event(eventData)
        }
}