package com.example.core_data.datadelegate

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