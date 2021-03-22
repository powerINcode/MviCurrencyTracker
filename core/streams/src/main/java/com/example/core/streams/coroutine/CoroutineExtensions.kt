package com.example.core.streams.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

fun <T> Flow<T>.onMainThread() = this.flowOn(Dispatchers.Main)
fun <T> Flow<T>.onIo() = this.flowOn(Dispatchers.IO)

suspend fun <T> onIo(block: suspend () -> T) : T = withContext(Dispatchers.IO) {
    block()
}
fun <T> Flow<T>.startWithEmitItem(item: T) = this.onStart { emit(item) }