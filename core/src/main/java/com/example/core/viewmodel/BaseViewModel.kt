package com.example.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.example.core.data.datadelegate.Data
import com.example.core.livedata.LiveEvent
import com.example.core.livedata.MutableLiveEvent
import com.example.core.routing.NavigationCommand
import com.example.core.rx.toLiveData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseViewModel<State : Any>(
    private val reducer: StateReducer<State>
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val stateFlow: LiveData<State>
        get() = reducer.stateFlow.toLiveData()

    protected val state: State get() = reducer.state

    private val _navigation: MutableLiveEvent<NavigationCommand> = MutableLiveEvent()
    val navigation: LiveEvent<NavigationCommand> get() = _navigation

    protected val intentSubject = BehaviorSubject.create<Any>()

    private val inited = AtomicBoolean(false)

    fun init() {
        LiveDataReactiveStreams.fromPublisher<Int> {  }
        if (inited.compareAndSet(false, true)) {
            doInit()
        }
    }

    protected abstract fun doInit()

    fun send(intent: Any) {
        intentSubject.onNext(intent)
    }

    fun navigate(command: NavigationCommand) {
        _navigation.event = command
    }

    protected inline fun <reified T : Any> intentOf(): Observable<T> = intentSubject.ofType(T::class.java)

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    protected fun <T> Observable<T>.subscribeTillClear(onError: (Throwable) -> Unit = {}, block: ((T) -> Unit) = {}) = compositeDisposable.add(this.subscribe(block, onError))
    protected fun <T> Maybe<T>.subscribeTillClear(onError: (Throwable) -> Unit = {}, block: ((T) -> Unit) = {}) = compositeDisposable.add(this.subscribe(block, onError))
    protected fun Completable.subscribeTillClear(onError: (Throwable) -> Unit = {}, block: (() -> Unit) = {}) = compositeDisposable.add(
        this.subscribe({
            block()
        }, {
            onError(it)
        })
    )

    protected fun <T> Observable<Data<T>>.extractContent(
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

}