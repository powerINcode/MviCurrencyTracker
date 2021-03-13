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
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    rateTrackerStateReducer: MainStateReducer,
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase,
    private val getMainCurrency: GetMainCurrencyRatesUseCase
) : BaseViewModel<RateTrackerIntent, RateTrackerState, RateChange>(rateTrackerStateReducer) {

    override fun getInitialState(): RateTrackerState = RateTrackerState.EMPTY
    override fun getInitialChange(): RateChange = RateChange.StartLoading

    @Volatile
    var infinityLoading: Boolean = true

    override fun doInit() {
        intentSubject.ofType(RateTrackerIntent.NavigateToInfo::class.java)
            .subscribeTillClear {
                navigate(NavigationCommand.FeatureCommand(ProfileFeatureConfig))
            }

        intentSubject.ofType(RateTrackerIntent.AmountUpdated::class.java).map { it.amount }
            .doOnNext { onChange(RateChange.UpdateAmount(it), RateChange.RecalculateAmounts) }
            .subscribeTillClear()


        intentSubject.ofType(RateTrackerIntent.CurrencySelected::class.java)
            .doOnNext {
                onChange(RateChange.SelectNewCurrency(it.currency))
            }
            .map { it.currency }
            .startWith(
                getMainCurrency()
                    .map {
                        ScreenCurrency(
                            name = it.name,
                            amount = DEFAULT_CURRENCY_VALUE,
                            rate = it.rate
                        )
                    }.defaultIfEmpty(
                        ScreenCurrency(
                            DEFAULT_CURRENCY,
                            DEFAULT_CURRENCY_VALUE,
                            DEFAULT_CURRENCY_RATE
                        )
                    )
            )
            .switchMap { currency ->
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
                    .doOnError {
                        val b = 0
                    }
                    .retryWhen { it.delay(1000, TimeUnit.MILLISECONDS) }
                    .takeWhile { it.loading }
                    .repeatWhen {
                        if (infinityLoading) {
                            it.flatMap { Observable.timer(1000, TimeUnit.MILLISECONDS) }
                        } else {
                            it
                        }
                    }
            }

            .subscribeTillClear()
    }
}