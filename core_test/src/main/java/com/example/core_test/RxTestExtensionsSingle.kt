package com.example.core_test

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

fun <T> OngoingStubbing<Single<T>>.thenEmit(item: T): OngoingStubbing<Single<T>> = this.thenReturn(Single.just(item))
fun <T> OngoingStubbing<Single<T>>.thenEmitError(t: Throwable): OngoingStubbing<Single<T>> = this.thenReturn(Single.error(t))
