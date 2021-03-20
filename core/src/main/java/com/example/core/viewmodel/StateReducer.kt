package com.example.core.viewmodel

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

import com.example.core.mvi.Change

interface StateReducer<State, C: Change> {
    fun reduce(state: State,change: C): State
}