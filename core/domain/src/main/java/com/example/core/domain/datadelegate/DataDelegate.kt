package com.example.core.domain.datadelegate

interface DataDelegate<Params, T> {
    suspend fun getFromMemory(): T?
    suspend fun putToMemory(data: T)

    suspend fun getFromStorage(): T?
    suspend fun putToStorage(data: T)

    suspend fun getFromNetwork(params: Params): T

}