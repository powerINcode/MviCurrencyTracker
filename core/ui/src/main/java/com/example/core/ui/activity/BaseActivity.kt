package com.example.core.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.core.application.ApiProvider
import com.example.core.domain.routing.Navigator
import com.example.core.domain.viewmodel.BaseViewModel
import com.example.core.streams.livedata.EventObserver
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<Component: Any, State: Any, VM: BaseViewModel<State>, ActivityBinding: ViewBinding>: AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected abstract val viewBinding: ActivityBinding

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    lateinit var component: Component
    protected val viewModel : VM by lazy { ViewModelProvider(this, modelFactory).get(getViewModelClass()) }

    protected abstract fun getViewModelClass(): Class<VM>
    protected abstract fun createComponent(): Component
    protected abstract fun inject(component: Component)

    override fun onStart() {
        super.onStart()

        viewModel.navigation.observe(this, EventObserver {
            navigator.navigate(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component = createComponent()
        inject(component)

        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewModel.stateFlow.observe(this, { render(it) })
        viewModel.init()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
    }

    protected abstract fun render(state: State)

    fun <T> Observable<T>.subscribeTillDestroy(block: (T) -> Unit)  {
        compositeDisposable.add(this.subscribe(block, {}, {}))
    }

    protected fun<T: Any> provideApi(api: Class<T>): T = (applicationContext as ApiProvider).getApi(api)
}