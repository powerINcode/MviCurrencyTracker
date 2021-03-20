package com.example.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.example.core.livedata.LiveEvent
import com.example.core.livedata.MutableLiveEvent
import com.example.core.routing.NavigationCommand
import com.example.core.rx.toLiveData
import com.example.core_data.datadelegate.Data
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseViewModel<Intent, State, C: Change>(
    private val reducer: StateReducer<State, C>
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state

    protected val _navigation: MutableLiveEvent<NavigationCommand> = MutableLiveEvent()
    val navigation: LiveEvent<NavigationCommand> = _navigation

    private val startState: State by lazy { getInitialState() }
    abstract fun getInitialState(): State

    private val startChange: C by lazy { getInitialChange() }
    abstract fun getInitialChange(): C

    protected val intentSubject = BehaviorSubject.create<Intent>()
    protected val changeSubject = BehaviorSubject.create<C>()

    private val inited = AtomicBoolean(false)

    fun init() {
        LiveDataReactiveStreams.fromPublisher<Int> {  }
        if (inited.compareAndSet(false, true)) {
            doInit()
        }
    }

    protected abstract fun doInit()

    fun send(intent: Intent) {
        intentSubject.onNext(intent)
    }

    fun navigate(command: NavigationCommand) {
        _navigation.event = command
    }

    @MainThread
    protected fun notify(state: State) {
        _state.value = state
    }

    protected fun onChange(vararg changes: C?) {
        changes.filterNotNull().forEach { change -> changeSubject.onNext(change) }
    }

    init {
        changeSubject
            .startWithItem(startChange)
            .observeOn(Schedulers.io())
            .concatMap {
                if (it is com.example.core.mvi.ChangeContainer) {
                    Observable.fromIterable(it.changes.map { it as C })
                } else {
                    Observable.just(it)
                }
            }
            .scan(startState, { state, change -> reducer.reduce(state, change) })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTillClear({
                Log.e(this.javaClass.canonicalName, it.localizedMessage ?: "Empty String")
            }, ::notify)
    }

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