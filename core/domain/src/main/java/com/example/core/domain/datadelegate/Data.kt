package com.example.core.domain.datadelegate

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

sealed class Data<out T> {
    abstract val content: T?

    data class Loading<T>(override val content: T? = null): Data<T>()
    data class Complete<T>(override val content: T): Data<T>()
    data class Error<T>(val error: Throwable, override val content: T? = null): Data<T>()
}

fun <T> T?.asLoadingData() = Data.Loading(content = this)
fun <T> T.asErrorData(e: Throwable) = Data.Error(content = this, error = e)
fun <T> T.asCompleteData() = Data.Complete(content = this)

val <T> Data<T>.isError: Boolean get() = this is Data.Error
val <T> Data<T>.isNotError: Boolean get() = this !is Data.Error
val <T> Data<T>.loading: Boolean get() = this is Data.Loading
val <T> Data<T>.complete: Boolean get() = this is Data.Complete

fun <T> Flow<Data<T>>.extractContent(
    dropCache: Boolean = false,
    onError: suspend (Throwable) -> Throwable? = { null },
    onContentEmpty: suspend () -> Unit = {},
    onContentAvailable: suspend (T) -> Unit = {},
    onContentLoaded: suspend (T) -> Unit = {},
): Flow<T> {
    return this.transformLatest { data ->
        when (data) {
            is Data.Error -> onError(data.error)?.let { throw it }
            is Data.Loading -> data.content?.let { cache ->
                onContentAvailable(cache)
                if (!dropCache) {
                    emit(cache)
                }
            } ?: onContentEmpty()
            is Data.Complete -> {
                onContentAvailable(data.content)
                onContentLoaded(data.content)
                emit(data.content)
            }
        }
    }
}