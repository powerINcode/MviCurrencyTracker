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
            is FeatureCommand -> {
                launchers.asSequence()
                    .firstOrNull { it.suitFor(command.config) }
                    ?.launch(this, command.config)
            }
            is Finish -> activity.finish()
            is ActivityCommand<*> -> activity.startActivity(Intent(activity, command.destination))
        }.let{}
    }

}