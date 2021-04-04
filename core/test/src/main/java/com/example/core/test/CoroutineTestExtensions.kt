package com.example.core.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.mockito.stubbing.OngoingStubbing

@ExperimentalCoroutinesApi
suspend fun <T> Flow<T>.test(scope: CoroutineScope, block: (suspend TestObserver<T>.() -> Unit)? = null): TestObserver<T> {
    return TestObserver(scope, this).apply {
        block?.let {
            block(this)
            finish()
        }
    }
}
// TODO think about this
fun <T> Flow<T>.collectToList(scope: CoroutineScope): List<T> {
    val list = mutableListOf<T>()
    onEach { list.add(it) }
        launchIn(scope)

    return list
}

fun <T> OngoingStubbing<T>.thenReturnEmpty(): OngoingStubbing<T?> = this.thenReturn(null)
fun <T> OngoingStubbing<T>.thenEmitError(error: Throwable): OngoingStubbing<T?> = this.thenThrow(error)
fun OngoingStubbing<Unit>.thenReturnComplete(): OngoingStubbing<Unit> = this.thenReturn(Unit)

fun <T> OngoingStubbing<Flow<T>>.thenEmit(vararg items: T): OngoingStubbing<Flow<T>> = this.thenReturn(flowOf(*items))
fun <T> OngoingStubbing<Flow<T>>.thenEmitEmpty(): OngoingStubbing<Flow<T>> = this.thenReturn(flowOf())
