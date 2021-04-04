package com.example.core.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseViewModel<State : Any, Reducer : StateReducer<State>> constructor(
    val reducer: Reducer
) : ViewModel() {

    val state: LiveData<State> get() = reducer.stateLiveData.asLiveData()

    protected val _state: State get() = reducer.state

    private val inited = AtomicBoolean(false)

    fun init() {
        runBlocking {
            if (inited.compareAndSet(false, true)) {
                doInit()
            }
        }
    }

    protected abstract suspend fun doInit()

    protected fun <T> Flow<T>.launchInScope() = this.launchIn(viewModelScope)
}