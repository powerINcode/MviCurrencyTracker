package com.example.feature_rate_tracker.impl

import androidx.annotation.VisibleForTesting
import com.example.core.domain.datadelegate.Data
import com.example.core.domain.datadelegate.extractContent
import com.example.core.domain.datadelegate.loading
import com.example.core.domain.viewmodel.BaseViewModel
import com.example.feature_rate_tracker.impl.MainScreenContract.*
import com.example.feature_rate_tracker_api.data.models.Advertisement
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    reducer: MainStateReducer,
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase,
    private val observeAdvertisement: ObserveAdvertisementUseCase,
    private val getMainCurrencyUseCase: GetMainCurrencyRatesUseCase
) : BaseViewModel<RateTrackerState, MainStateReducer>(reducer) {

    @Volatile
    @VisibleForTesting
    var infinityLoading: Boolean = true

    private val _advertisementFlow = MutableStateFlow<List<Advertisement>>(emptyList())
    val advertisementFlow: Flow<List<Advertisement>> = _advertisementFlow

    override suspend fun doInit() {
        observeAdvertisement()
            .extractContent(dropCache = true)
            .onEach(_advertisementFlow::emit)
            .launchInScope()
    }

    suspend fun getInitialCurrency(): Currency = getMainCurrencyUseCase() ?: _state.currency

    fun getInitialCurrencyAmount(): Double = _state.amount

    suspend fun observeLatestCurrenciesFor(currency: Currency): Flow<Data<List<Currency>>> {
        return observeCurrencyRates(currency.name)
            .retryWhen { _, _ ->
                delay(RETRY_DELAY)
                true
            }
            .transformWhile { data ->
                emit(data)
                data.loading
            }
            .repeatWithDelay(RETRY_DELAY)
    }

    private suspend fun <T> Flow<Data<T>>.repeatWithDelay(delay: Long) =
        if (infinityLoading) {
            flow {
                while (currentCoroutineContext().isActive) {
                    this@repeatWithDelay.collect {
                        emit(it)
                    }
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