package com.example.core.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.core.livedata.EventObserver
import com.example.core.routing.Navigator
import com.example.core.viewmodel.BaseViewModel
import javax.inject.Inject

abstract class BaseActivity<Intent, State, VM: BaseViewModel<Intent, State, *>>: AppCompatActivity() {

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

        viewModel.state.observe(this, Observer { render(it) })
        viewModel.init()
    }

    protected abstract fun render(state: State)
}