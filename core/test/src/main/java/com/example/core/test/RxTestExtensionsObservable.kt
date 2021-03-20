package com.example.core.test

import io.reactivex.rxjava3.core.Observable
import org.mockito.stubbing.OngoingStubbing


fun <T> OngoingStubbing<Observable<T>>.thenEmit(item: T): OngoingStubbing<Observable<T>> = this.thenReturn(Observable.just(item))
fun <T> OngoingStubbing<Observable<T>>.thenEmitEmpty(): OngoingStubbing<Observable<T>> = this.thenReturn(Observable.empty())
