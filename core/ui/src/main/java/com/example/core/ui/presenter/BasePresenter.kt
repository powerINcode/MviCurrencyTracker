package com.example.core.ui.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.routing.NavigationCommand
import com.example.core.domain.viewmodel.BaseViewModel
import com.example.core.domain.viewmodel.StateReducer
import com.example.core.rx.toLiveData
import com.example.core.streams.livedata.LiveEvent
import com.example.core.streams.livedata.MutableLiveEvent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.Maybes
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BasePresenter<State : Any, Reducer : StateReducer<State>, T : BaseViewModel<State, Reducer>> : Presenter {
    private var _viewModel: T? = null
    protected val viewModel: T get() = requireNotNull(_viewModel)

    protected val reducer: Reducer get() = viewModel.reducer

    val stateLiveData: LiveData<State> get() = reducer.stateLiveData.toLiveData()

    protected val state: State get() = reducer.state

    private val _navigation: MutableLiveEvent<NavigationCommand> = MutableLiveEvent()
    val navigation: LiveEvent<NavigationCommand> get() = _navigation

    protected val intentSharedFlow: PublishSubject<Any> = PublishSubject.create()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onAttach() {

    }

    override fun onCreate(vm: ViewModel) {
        _viewModel = vm as T
        vm.init()
    }

    override fun onCreated() {

    }

    override fun onDetach() {}

    override fun onDestroy() {
        compositeDisposable.clear()
        _viewModel = null
    }

    override fun <State : Any> observeStateChange(lifecycleOwner: LifecycleOwner, block: (State) -> Unit) {
        stateLiveData.observe(lifecycleOwner) { newState -> (newState as? State)?.let(block) }
    }

    fun send(intent: Any) {
        intentSharedFlow.onNext(intent)
    }

    fun navigate(command: NavigationCommand) {
        _navigation.event = command
    }

    protected inline fun <reified T : Any> intentOf(): Observable<T> = intentSharedFlow.ofType(T::class.java)

    protected fun <T> Observable<T>.subscribeTillAttach(onError: (Throwable) -> Unit = {}, block: ((T) -> Unit) = {}) = compositeDisposable.add(this.subscribe(block, onError))
    protected fun <T> Maybe<T>.subscribeTillAttach(onError: (Throwable) -> Unit = {}, block: ((T) -> Unit) = {}) = compositeDisposable.add(this.subscribe(block, onError))
    protected fun Completable.subscribeTillAttach(onError: (Throwable) -> Unit = {}, block: (() -> Unit) = {}) = compositeDisposable.add(
        this.subscribe({
            block()
        }, {
            onError(it)
        })
    )
}