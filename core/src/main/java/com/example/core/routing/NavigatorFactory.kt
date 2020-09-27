package com.example.core.routing

import androidx.appcompat.app.AppCompatActivity
import com.example.core.di.CoreComponentProvider

object NavigatorFactory {
    fun create(activity: AppCompatActivity): Navigator {
        return NavigatorImpl(
            activity = activity,
            launchers = CoreComponentProvider.instance.getFeatureLaunchers()
        )
    }
}