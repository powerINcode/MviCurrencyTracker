package com.example.feature_rate_tracker_impl

import androidx.annotation.VisibleForTesting
import com.example.core.routing.NavigationCommand
import com.example.core.viewmodel.BaseViewModel
import com.example.feature_profile_api.declaration.ProfileFeatureConfig
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.example.feature_rate_tracker_impl.MainScreenContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val reducer: MainStateReducer,
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase,
    private val observeAdvertisement: ObserveAdvertisementUseCase,
    private val getMainCurrency: GetMainCurrencyRatesUseCase
) : BaseViewModel<RateTrackerState>(reducer) {

    @Volatile
    @VisibleForTesting
    var infinityLoading: Boolean = true

    @ExperimentalCoroutinesApi
    override suspend fun doInit() {
        intentOf<RateTrackerIntent.NavigateToInfo>()
            .collectInScope {
                navigate(NavigationCommand.FeatureCommand(ProfileFeatureConfig))
            }

        intentOf<RateTrackerIntent.AmountUpdated>()
            .map { it.amount }
            .onEach { reducer.updateAmount(it) }
            .collectInScope()

        observeAdvertisement()
            .extractContent(dropCache = true)
            .collectInScope {
                reducer.addAdvertisements(it)
            }


        intentOf<RateTrackerIntent.CurrencySelected>()
            .onStart {
                val currency = getMainCurrency() ?: state.currency
                emit(RateTrackerIntent.CurrencySelected(currency, state.amount))
            }
            .onEach { reducer.selectCurrency(it.currency, it.amount) }
            .map { it.currency }
            .flatMapLatest { currency ->
                observeCurrencyRates(currency.name)
                    .extractContent(
                        dropCache = true,
                        onError = { e ->
                            reducer.ratesLoadingError()
                            e
                        },
                        onContentEmpty = {
                            reducer.startLoadingRates()
                        },
                        onContentAvailable = {
                            reducer.ratesLoaded(it)
                        }
                    )
                    .retryWhen { _, _ ->
                        delay(RETRY_DELAY)
                        true

                    }
                    .take(1)
                    .repeatWithDelay(RETRY_DELAY)
            }
            .collectInScope()
    }

    private suspend fun <T> Flow<T>.repeatWithDelay(delay: Long) =
        if (infinityLoading) {
            flow {
                while (currentCoroutineContext().isActive) {
                    this@repeatWithDelay.collect { emit(it) }
                    delay(delay)
                }

            }
        } else {
            this
        }

    companion object {
        private const val RETRY_DELAY = 1000L
    }
}