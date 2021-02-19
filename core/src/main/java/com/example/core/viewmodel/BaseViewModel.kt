package com.example.core.viewmodel

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.coroutine.onIo
import com.example.core.coroutine.onMainThread
import com.example.core.coroutine.startWithEmitItem
import com.example.core.livedata.LiveEvent
import com.example.core.livedata.MutableLiveEvent
import com.example.core.mvi.Change
import com.example.core.routing.NavigationCommand
import com.example.core_data.datadelegate.Data
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseViewModel<Intent, State, C : Change>(
    private val reducer: StateReducer<State, C>
) : ViewModel() {

    protected val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state

    protected val _navigation: MutableLiveEvent<NavigationCommand> = MutableLiveEvent()
    val navigation: LiveEvent<NavigationCommand> = _navigation

    private val startState: State by lazy { getInitialState() }
    abstract fun getInitialState(): State

    private val startChange: C by lazy { getInitialChange() }
    abstract fun getInitialChange(): C

    protected val intentSharedFlow = MutableSharedFlow<Intent>(replay = 1)
    protected val changeSharedFlow = MutableSharedFlow<C>(replay = 1)

    private val inited = AtomicBoolean(false)

    fun init() {
        runBlocking {
            if (inited.compareAndSet(false, true)) {
                doInit()
            }
        }
    }

    protected abstract suspend fun doInit()

    fun send(intent: Intent) {
        intentSharedFlow.tryEmit(intent)
    }

    fun navigate(command: NavigationCommand) {
        _navigation.event = command
    }

    @MainThread
    protected fun notify(state: State) {
        _state.value = state
    }

    protected suspend fun onChange(vararg changes: C?) {
        changes.filterNotNull().forEach { change -> changeSharedFlow.emit(change) }
    }

    init {
        changeSharedFlow
            .startWithEmitItem(startChange)
            .onIo()
            .flatMapConcat {
                if (it is com.example.core.mvi.ChangeContainer) {
                    flowOf(it.changes as C)
                } else {
                    flowOf(it as C)
                }
            }
            .scan(startState, { state, change -> reducer.reduce(state, change) })
            .onMainThread()
            .collectInScope(
                onError = {
                    Log.e(this.javaClass.canonicalName, it.localizedMessage ?: "Empty String")
                },
                block = {
                    notify(it)
                })
    }

    protected fun <T> Flow<T>.collectInScope(
        onError: suspend FlowCollector<T>.(Throwable) -> Unit = {},
        block: (suspend (T) -> Unit) = {}
    ) = this.catch(onError)
        .onEach { block(it) }
        .launchIn(viewModelScope)

    protected fun <T> Flow<Data<T>>.onDataAvailable(block: suspend (T) -> Unit) =
        this.onEach { it.data?.let { block(it) } }

    protected fun <T> Flow<Data<T>>.onDataError(block: suspend (e: Throwable) -> Unit) =
        this.onEach {
            if (it is Data.Error) {
                block(it.error)
            }
        }

    protected fun <T> Flow<Data<T>>.onDataNotError(block: suspend (T?) -> Unit) = this.onEach {
        if (it !is Data.Error) {
            block(it.data)
        }
    }

    protected fun <T> Flow<Data<T>>.extractError() = this.map {
        if (it is Data.Error) {
            throw it.error
        } else {
            it
        }
    }

}