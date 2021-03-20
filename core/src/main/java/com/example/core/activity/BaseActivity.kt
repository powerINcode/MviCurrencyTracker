package com.example.core.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.core.livedata.EventObserver
import com.example.core.routing.Navigator
import com.example.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

abstract class BaseActivity<State: Any, VM: BaseViewModel<State>, ActivityBinding: ViewBinding>: AppCompatActivity() {

    protected abstract val viewBinding: ActivityBinding

    @Inject
    lateinit var navigator: Navigator

    protected abstract val viewModel: VM

    override fun onStart() {
        super.onStart()

        viewModel.navigation.observe(this, EventObserver {
            navigator.navigate(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewModel.stateFlow.observe(this, { render(it) })
        viewModel.init()
    }

    protected abstract fun render(state: State)

    fun <T> Flow<T>.collectWhenCreated(block: (T) -> Unit) = lifecycleScope.launchWhenCreated {
        collect { block(it) }
    }
}