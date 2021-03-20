package com.example.core.data.datadelegate

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface DataDelegate<Params, T> {
    fun getFromMemory(): Maybe<T>
    fun putToMemory(data: T): Completable

    fun getFromStorage(): Maybe<T>
    fun putToStorage(data: T): Completable

    fun getFromNetwork(params: Params): Single<T>

}