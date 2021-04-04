package com.example.core.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.core.application.ApiProvider
import com.example.core.ui.presenter.Presenter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<Component: Any, State : Any, P : Presenter, VM: ViewModel> :
    AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected abstract val viewBinding: ViewBinding

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    protected abstract var presenter: P

    protected val viewModel : VM by lazy { ViewModelProvider(this, modelFactory).get(getViewModelClass()) }

    lateinit var component: Component

    protected abstract fun getViewModelClass(): Class<VM>
    protected abstract fun createComponent(): Component
    protected abstract fun inject(component: Component)

    override fun onCreate(savedInstanceState: Bundle?) {
        component = createComponent()
        inject(component)

        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        presenter.onCreate(viewModel)

        presenter.observeStateChange<State>(this) { render(it) }

        presenter.onCreated()
    }

    override fun onResume() {
        super.onResume()

        presenter.onAttach()
    }

    override fun onPause() {
        super.onPause()

        presenter.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
        presenter.onDestroy()
    }

    protected abstract fun render(state: State)

    fun <T> Observable<T>.subscribeTillDestroy(block: (T) -> Unit)  {
        compositeDisposable.add(this.subscribe(block, {}, {}))
    }

    protected fun<T: Any> provideApi(api: Class<T>): T = (applicationContext as ApiProvider).getApi(api)
}