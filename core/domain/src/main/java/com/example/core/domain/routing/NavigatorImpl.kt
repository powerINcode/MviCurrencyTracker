package com.example.core.domain.routing

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class NavigatorImpl @Inject constructor(
    @ActivityContext private val context: Context,
    private val launchers: Set<@JvmSuppressWildcards FeatureLauncher>
): Navigator {
    private val activity: AppCompatActivity get() = context as AppCompatActivity

    override fun navigate(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.FeatureCommand -> {
                launchers
                    .firstOrNull { it.suitFor(command.config) }
                    ?.launch(this, command.config)
            }
            is NavigationCommand.Finish -> activity.finish()
            is NavigationCommand.ActivityCommand<*> -> activity.startActivity(Intent(activity, command.destination))
        }.let{}
    }

}