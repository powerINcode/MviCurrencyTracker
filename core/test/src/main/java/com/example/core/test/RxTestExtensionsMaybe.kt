package com.example.core.test

import io.reactivex.rxjava3.core.Maybe
import org.mockito.stubbing.OngoingStubbing

fun <T> OngoingStubbing<Maybe<T>>.thenEmit(item: T): OngoingStubbing<Maybe<T>> = this.thenReturn(Maybe.just(item))
fun <T> OngoingStubbing<Maybe<T>>.thenEmitEmpty(): OngoingStubbing<Maybe<T>> = this.thenReturn(Maybe.empty())
