package com.example.core.streams.livedata

import androidx.lifecycle.LiveData

open class LiveEvent<T>: LiveData<Event<T>>()