package com.example.core.data.datadelegate

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class DataManager<Params, T>(private val delegate: DataDelegate<Params, T>) {
    private val subject = MutableSharedFlow<Data<T>>(replay = 1)

    suspend fun observe(forceReload: Boolean, params: Params): Flow<Data<T>> = flow {
        val memoryCache = delegate.getFromMemory()
        val result = if (memoryCache == null) {
            val storageCache = delegate.getFromStorage()
            if (storageCache != null) {
                update(storageCache)
                storageCache
            } else {
                null
            }
        } else {
            memoryCache
        }?.let { it.asCompleteData() } ?: Data.Loading<T>()

        emit(result)
    }
        .flatMapLatest { data ->
            if (forceReload || data is Data.Loading) {
                loadAndUpdateCache(params)
                    .catch { emit(Data.Error(it, data.content)) }
                    .onStart { emit(Data.Loading(data.content)) }
            } else {
                flowOf(data)
            }
        }
        .onEach(subject::emit)
        .onCompletion { emitAll(subject.drop(1)) }
        .flowOn(Dispatchers.IO)

    suspend fun update(data: T) {
        delegate.putToMemory(data)
        delegate.putToStorage(data)
        subject.emit(data.asCompleteData())
    }

    private fun loadAndUpdateCache(params: Params): Flow<Data<T>> = flow {
        val value = delegate.getFromNetwork(params)
        update(value)
        emit(value.asCompleteData())
    }
}