package com.example.core.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.core.ui.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

abstract class BaseActivity<State : Any, P : Presenter, VM: ViewModel> :
    AppCompatActivity() {

    protected abstract val viewBinding: ViewBinding

    protected abstract var presenter: P

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        presenter.onCreate(lifecycleScope, viewModel)

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

        presenter.onDestroy()
    }

    protected inline fun <reified T : ViewModel> activityViewModel() = this.viewModels<T>().value

    protected abstract fun render(state: State)

    fun <T> Flow<T>.collectWhenCreated(block: (T) -> Unit) = lifecycleScope.launchWhenCreated {
        collect { block(it) }
    }
}