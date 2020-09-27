package com.example.core.livedata

import androidx.lifecycle.LiveData

open class LiveEvent<T>: LiveData<Event<T>>()