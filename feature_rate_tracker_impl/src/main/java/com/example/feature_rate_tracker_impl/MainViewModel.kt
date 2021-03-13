package com.example.feature_rate_tracker_impl

import com.example.core.routing.NavigationCommand
import com.example.core.viewmodel.BaseViewModel
import com.example.core_data.datadelegate.loading
import com.example.feature_profile_api.declaration.ProfileFeatureConfig
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.example.feature_rate_tracker_impl.MainScreenContract.*
import com.example.feature_rate_tracker_impl.MainScreenContract.Companion.DEFAULT_CURRENCY
import com.example.feature_rate_tracker_impl.MainScreenContract.Companion.DEFAULT_CURRENCY_RATE
import com.example.feature_rate_tracker_impl.MainScreenContract.Companion.DEFAULT_CURRENCY_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    reducer: MainStateReducer,
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase,
    private val getMainCurrency: GetMainCurrencyRatesUseCase
) : BaseViewModel<RateTrackerIntent, RateTrackerState, RateChange>(reducer) {

    override fun getInitialState(): RateTrackerState = RateTrackerState.EMPTY
    override fun getInitialChange(): RateChange = RateChange.StartLoading

    @Volatile
    var infinityLoading: Boolean = true

    override suspend fun doInit() {
        intentSharedFlow.filterIsInstance<RateTrackerIntent.NavigateToInfo>()
            .collectInScope {
                navigate(NavigationCommand.FeatureCommand(ProfileFeatureConfig))
            }

        intentSharedFlow.filterIsInstance<RateTrackerIntent.AmountUpdated>()
            .map { it.amount }
            .onEach { onChange(RateChange.UpdateAmount(it), RateChange.RecalculateAmounts) }
            .collectInScope()


        intentSharedFlow.filterIsInstance<RateTrackerIntent.CurrencySelected>()
            .onEach {
                onChange(RateChange.SelectNewCurrency(it.currency))
            }
            .map { it.currency }
            .onStart {
                emit(getMainCurrency()?.let {
                    ScreenCurrency(
                        name = it.name,
                        amount = DEFAULT_CURRENCY_VALUE,
                        rate = it.rate
                    )
                } ?: ScreenCurrency(
                    DEFAULT_CURRENCY,
                    DEFAULT_CURRENCY_VALUE,
                    DEFAULT_CURRENCY_RATE
                ))
            }
            .flatMapLatest { currency ->
                observeCurrencyRates(currency.name)
                    .onDataAvailable {
                        onChange(
                            RateChange.StopLoading,
                            RateChange.UpdateRates(it),
                            RateChange.RecalculateAmounts
                        )
                    }
                    .onDataError { onChange(RateChange.Error) }
                    .onDataNotError { onChange(RateChange.HideError) }
                    .extractError()
                    .retryWhen { _, _ ->
                        delay(1000)
                        true

                    }
                    .takeWhile { it.loading }
                    .repeatWithDelay(1000)
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
}