package com.example.core.test

import io.reactivex.rxjava3.core.Observable
import org.mockito.stubbing.OngoingStubbing


fun <T> OngoingStubbing<Observable<T>>.thenEmit(vararg items: T): OngoingStubbing<Observable<T>> =
    this.thenReturn(Observable.fromArray(*items))

fun <T> OngoingStubbing<Observable<T>>.thenEmitEmpty(): OngoingStubbing<Observable<T>> = this.thenReturn(Observable.empty())
