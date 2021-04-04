package com.example.core.domain.datadelegate

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

sealed class Data<T> {
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

fun <T> Observable<Data<T>>.extractContent(
    dropCache: Boolean = false,
    onError: (Throwable) -> Throwable? = { null },
    onContentEmpty: () -> Unit = {},
    onContentAvailable: (T) -> Unit = {},
    onContentLoaded: (T) -> Unit = {},
): Observable<T> {
    return this.switchMap { data ->
        Observable.create<T> { emitter ->
            when (data) {
                is Data.Error -> onError(data.error)?.let { emitter.onError(it) }
                is Data.Loading -> data.content?.let { cache ->
                    onContentAvailable(cache)
                    if (!dropCache) {
                        emitter.onNext(cache)
                    }
                } ?: onContentEmpty()
                is Data.Complete -> {
                    onContentAvailable(data.content)
                    onContentLoaded(data.content)
                    emitter.onNext(data.content)
                }
            }
        }
    }
}