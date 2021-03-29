package com.example.feature_rate_tracker.impl

import androidx.annotation.VisibleForTesting
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerIntent
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerState
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val rateTrackerStateReducer: MainStateReducer,
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase,
    private val observeAdvertisement: ObserveAdvertisementUseCase,
    private val getMainCurrency: GetMainCurrencyRatesUseCase
) : com.example.core.domain.viewmodel.BaseViewModel<RateTrackerState>(rateTrackerStateReducer) {

    @Volatile
    @VisibleForTesting
    var infinityLoading: Boolean = true

    override fun doInit() {
        intentOf<RateTrackerIntent.NavigateToInfo>()
            .subscribeTillClear {
                navigate(com.example.core.domain.routing.NavigationCommand.FeatureCommand(com.example.feature_profile.api.declaration.ProfileFeatureConfig))
            }

        intentOf<RateTrackerIntent.AmountUpdated>()
            .map { it.amount }
            .doOnNext { rateTrackerStateReducer.updateAmount(it) }
            .subscribeTillClear()

        observeAdvertisement()
            .extractContent(dropCache = true)
            .subscribeTillClear {
                rateTrackerStateReducer.addAdvertisements(it)
            }


        intentOf<RateTrackerIntent.CurrencySelected>()
            .startWith(
                getMainCurrency()
                    .defaultIfEmpty(state.currency)
                    .map { currency -> RateTrackerIntent.CurrencySelected(currency, state.amount) }
                    .toObservable()
            )
            .doOnNext { rateTrackerStateReducer.selectCurrency(it.currency, it.amount) }
            .map { it.currency }
            .switchMap { currency ->
                observeCurrencyRates(currency.name)
                    .extractContent(
                        dropCache = true,
                        onError = { e ->
                            rateTrackerStateReducer.ratesLoadingError()
                            e
                        },
                        onContentEmpty = {
                            rateTrackerStateReducer.startLoadingRates()
                        },
                        onContentAvailable = {
                            rateTrackerStateReducer.ratesLoaded(it)
                        }
                    )
                    .retryWhen { it.delay(DELAY_TIME, TimeUnit.MILLISECONDS) }
                    .take(1)
                    .repeatWhen {
                        if (infinityLoading) {
                            it.flatMap { Observable.timer(DELAY_TIME, TimeUnit.MILLISECONDS) }
                        } else {
                            it
                        }
                    }
            }
            .subscribeTillClear()
    }

    companion object {
        private const val DELAY_TIME = 1000L
    }
}