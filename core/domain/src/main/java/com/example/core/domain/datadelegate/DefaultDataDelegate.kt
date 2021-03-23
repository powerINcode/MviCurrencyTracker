package com.example.core.domain.datadelegate

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

abstract class DefaultDataDelegate<Params, T>: DataDelegate<Params, T> {
    override fun getFromMemory(): Maybe<T> = Maybe.fromCallable { getFromMemoryItem() }
    abstract fun getFromMemoryItem(): T?

    override fun putToMemory(data: T): Completable = Completable.fromAction { putToMemoryCompletable(data) }
    abstract fun putToMemoryCompletable(data: T)

    override fun getFromStorage(): Maybe<T> = Maybe.fromCallable { getFromStorageItem() }
    abstract fun getFromStorageItem(): T?

    override fun putToStorage(data: T): Completable = Completable.fromAction { putToStorageCompletable(data) }
    abstract fun putToStorageCompletable(data: T)
}