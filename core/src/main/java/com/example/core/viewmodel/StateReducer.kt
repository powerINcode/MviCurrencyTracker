package com.example.core.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class StateReducer<State: Any>(initialState: State) {
    private val _stateFlow: MutableStateFlow<State> = MutableStateFlow(initialState)
    val stateFlow: StateFlow<State> = _stateFlow

    val state: State
        get() = _stateFlow.value

    protected fun State.commit() {
       _stateFlow.value = this
    }
}