package com.example.core_data.datadelegate

sealed class Data<out T> {
    abstract val data: T?

    data class Loading<T>(override val data: T? = null): Data<T>()
    data class Complete<T>(override val data: T): Data<T>()
    data class Error<T>(val error: Throwable, override val data: T? = null): Data<T>()
}

fun <T> T?.asLoadingData() = Data.Loading(data = this)
fun <T> T.asErrorData(e: Throwable) = Data.Error(data = this, error = e)
fun <T> T.asCompleteData() = Data.Complete(data = this)

val <T> Data<T>.isError: Boolean get() = this is Data.Error
val <T> Data<T>.isNotError: Boolean get() = this !is Data.Error
val <T> Data<T>.loading: Boolean get() = this is Data.Loading
val <T> Data<T>.complete: Boolean get() = this is Data.Complete