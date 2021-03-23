package com.example.core.ui.activity.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.di.scopes.ActivityScope
import com.example.core.domain.routing.Navigator
import com.example.core.domain.routing.NavigatorFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class BaseActivityModule {

    @ActivityScope
    @Provides
    fun provideNavigator(activity: AppCompatActivity): Navigator =
        NavigatorFactory.create(activity)

    @ActivityScope
    @Provides
    fun provideViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            var creator: Provider<out ViewModel>? = creators[modelClass]

            if (creator == null) {
                for ((key, value) in creators) {
                    if (modelClass.isAssignableFrom(key)) {
                        creator = value
                        break
                    }
                }
            }

            if (creator == null) {
                throw IllegalArgumentException("unknown model class $modelClass")
            }

            try {
                return creator.get() as T
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    companion object {

    }
}