package com.example.core.domain.viewmodel

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject


abstract class StateReducer<State: Any>(initialState: State) {
    private val _stateFlow: BehaviorSubject<State> = BehaviorSubject.createDefault(initialState)
    val stateFlow: Observable<State> = _stateFlow

    val state: State
        get() = _stateFlow.value

    protected fun State.commit() {
       _stateFlow.onNext(this)
    }
}