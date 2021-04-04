package com.example.core.ui.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.routing.NavigationCommand
import com.example.core.domain.viewmodel.BaseViewModel
import com.example.core.domain.viewmodel.StateReducer
import com.example.core.streams.livedata.LiveEvent
import com.example.core.streams.livedata.MutableLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BasePresenter<State: Any, Reducer: StateReducer<State>, T: BaseViewModel<State, Reducer>>: Presenter {
    private var _viewModel: T? = null
    protected val viewModel: T get() = requireNotNull(_viewModel)

    private var scope: CoroutineScope? = null

    protected val reducer: Reducer get() = viewModel.reducer

    val stateLiveData: LiveData<State> get() = reducer.stateLiveData.asLiveData()

    protected val state: State get() = reducer.state

    private val _navigation: MutableLiveEvent<NavigationCommand> = MutableLiveEvent()
    val navigation: LiveEvent<NavigationCommand> get() = _navigation

    protected val intentSharedFlow = MutableSharedFlow<Any>()

    override fun onAttach() {

    }

    override fun onCreate(scope: CoroutineScope, vm: ViewModel) {
        this.scope = scope
        _viewModel = vm as T
        vm.init()
    }

    override fun onCreated() {

    }

    override fun onDetach() {}

    override fun onDestroy() {
        _viewModel = null
    }

    override fun <State : Any> observeStateChange(lifecycleOwner: LifecycleOwner, block: (State) -> Unit) {
        stateLiveData.observe(lifecycleOwner,) { newState -> (newState as? State)?.let(block) }
    }

    fun send(intent: Any) {
        scope?.launch {
            intentSharedFlow.emit(intent)
        }
    }

    fun navigate(command: NavigationCommand) {
        _navigation.event = command
    }

    protected inline fun <reified T: Any> intentOf(): Flow<T> = intentSharedFlow.filterIsInstance()

    protected fun <T> Flow<T>.collectInScope(
        onError: suspend FlowCollector<T>.(Throwable) -> Unit = {},
        block: (suspend (T) -> Unit) = {}
    ) {
        val scope = requireNotNull(scope)
        this.catch(onError)
            .onEach { block(it) }
            .launchIn(scope)
    }
}