package com.example.core.viewmodel

import com.example.core.mvi.Change

interface StateReducer<State, C: Change> {
    fun reduce(state: State,change: C): State
}