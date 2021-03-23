package com.example.core.domain.routing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

class NavigatorImpl @Inject constructor(
    private val activity: AppCompatActivity,
    private val launchers: Set<@JvmSuppressWildcards FeatureLauncher>
): Navigator {
    override fun navigate(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.FeatureCommand -> {
                launchers.asSequence()
                    .firstOrNull { it.suitFor(command.config) }
                    ?.launch(this, command.config)
            }
            is NavigationCommand.Finish -> activity.finish()
            is NavigationCommand.ActivityCommand<*> -> activity.startActivity(Intent(activity, command.destination))
        }.let{}
    }

}