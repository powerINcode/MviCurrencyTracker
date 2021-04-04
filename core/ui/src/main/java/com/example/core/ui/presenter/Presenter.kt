package com.example.core.ui.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope

interface Presenter {

    fun onCreate(scope: CoroutineScope, vm: ViewModel)

    fun onCreated()

    fun onAttach()

    fun onDetach()

    fun onDestroy()

    fun <State: Any> observeStateChange(lifecycleOwner: LifecycleOwner, block: (State) -> Unit)
}