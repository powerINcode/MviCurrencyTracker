package com.example.feature_rate_tracker_impl

import androidx.annotation.VisibleForTesting
import com.example.core.routing.NavigationCommand
import com.example.core.viewmodel.BaseViewModel
import com.example.feature_profile_api.declaration.ProfileFeatureConfig
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.example.feature_rate_tracker_impl.MainScreenContract.RateTrackerIntent
import com.example.feature_rate_tracker_impl.MainScreenContract.RateTrackerState
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val rateTrackerStateReducer: MainStateReducer,
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase,
    private val observeAdvertisement: ObserveAdvertisementUseCase,
    private val getMainCurrency: GetMainCurrencyRatesUseCase
) : BaseViewModel<RateTrackerState>(rateTrackerStateReducer) {

    @Volatile
    @VisibleForTesting
    var infinityLoading: Boolean = true

    override fun doInit() {
        intentOf<RateTrackerIntent.NavigateToInfo>()
            .subscribeTillClear {
                navigate(NavigationCommand.FeatureCommand(ProfileFeatureConfig))
            }

        intentOf<RateTrackerIntent.AmountUpdated>()
            .map { it.amount }
            .doOnNext { rateTrackerStateReducer.updateAmount(it) }
            .subscribeTillClear()


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