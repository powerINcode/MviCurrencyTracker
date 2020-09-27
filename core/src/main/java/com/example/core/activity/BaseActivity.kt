package com.example.core.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.core.application.ApiProvider
import com.example.core.livedata.EventObserver
import com.example.core.routing.NavigationCommand
import com.example.core.routing.Navigator
import com.example.core.viewmodel.BaseViewModel
import javax.inject.Inject

abstract class BaseActivity<Component: Any, Intent, State, VM: BaseViewModel<Intent, State, *>>: AppCompatActivity() {
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

        viewModel.state.observe(this, Observer { render(it) })

        viewModel.init()
    }

    protected abstract fun render(state: State)

    protected fun<T: Any> provideApi(api: Class<T>): T = (applicationContext as ApiProvider).getApi(api)
}