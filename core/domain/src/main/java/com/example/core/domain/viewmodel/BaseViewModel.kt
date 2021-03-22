package com.example.core.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.domain.datadelegate.Data
import com.example.core.domain.routing.NavigationCommand
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseViewModel<State: Any>(
    private val reducer: StateReducer<State>
) : ViewModel() {

    val stateFlow: LiveData<State> get() = reducer.stateFlow
        .asLiveData()

    protected val state: State get() = reducer.state

    private val _navigation: com.example.core.streams.livedata.MutableLiveEvent<NavigationCommand> =
        com.example.core.streams.livedata.MutableLiveEvent()
    val navigation: com.example.core.streams.livedata.LiveEvent<NavigationCommand> get() = _navigation

    protected val intentSharedFlow = MutableSharedFlow<Any>(replay = 1)

    private val inited = AtomicBoolean(false)

    fun init() {
        runBlocking {
            if (inited.compareAndSet(false, true)) {
                doInit()
            }
        }
    }

    protected abstract suspend fun doInit()

    fun send(intent: Any) {
        intentSharedFlow.tryEmit(intent)
    }

    fun navigate(command: NavigationCommand) {
        _navigation.event = command
    }

    protected inline fun <reified T: Any> intentOf(): Flow<T> = intentSharedFlow.filterIsInstance()

    protected fun <T> Flow<T>.collectInScope(
        onError: suspend FlowCollector<T>.(Throwable) -> Unit = {},
        block: (suspend (T) -> Unit) = {}
    ) = this.catch(onError)
        .onEach { block(it) }
        .launchIn(viewModelScope)

    protected fun <T> Flow<Data<T>>.extractContent(
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

}