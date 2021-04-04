package com.example.feature_rate_tracker.impl

import androidx.annotation.VisibleForTesting
import com.example.core.domain.datadelegate.Data
import com.example.core.domain.datadelegate.extractContent
import com.example.core.domain.datadelegate.loading
import com.example.core.domain.viewmodel.BaseViewModel
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerIntent
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerState
import com.example.feature_rate_tracker_api.data.models.Advertisement
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    rateTrackerStateReducer: MainStateReducer,
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase,
    private val observeAdvertisement: ObserveAdvertisementUseCase,
    private val getMainCurrencyUseCase: GetMainCurrencyRatesUseCase
) : BaseViewModel<RateTrackerState, MainStateReducer>(rateTrackerStateReducer) {

    @Volatile
    @VisibleForTesting
    var infinityLoading: Boolean = true

    private val _advertisementFlow = PublishSubject.create<List<Advertisement>>()
    val advertisementFlow: Observable<List<Advertisement>> = _advertisementFlow

    override fun doInit() {
        observeAdvertisement()
            .extractContent(dropCache = true)
            .doOnNext(_advertisementFlow::onNext)
            .subscribeTillClear()
    }

    fun getInitialCurrency(): Single<Currency> = getMainCurrencyUseCase()
        .defaultIfEmpty(_state.currency)

    fun getInitialCurrencyAmount(): Double = _state.amount

    fun observeLatestCurrenciesFor(currency: Currency): Observable<Data<List<Currency>>> {
        return observeCurrencyRates(currency.name)
            .retryWhen { it.delay(DELAY_TIME, TimeUnit.MILLISECONDS) }
            .transformWhile { data ->
                data.loading
            }
            .repeatWithDelay(DELAY_TIME)
    }

    private fun <T> Observable<T>.transformWhile(block: (T) -> Boolean) = Observable.create<T> { emmiter ->
        var s: Disposable? = null
        s = this.subscribe {
            emmiter.onNext(it)
            if (!block(it)) {
                s?.dispose()
                emmiter.onComplete()
            }
        }

        emmiter.setCancellable {
            s?.dispose()
        }
    }

    private fun <T> Observable<Data<T>>.repeatWithDelay(delay: Long) = this.repeatWhen {
        if (infinityLoading) {
            it.flatMap { Observable.timer(delay, TimeUnit.MILLISECONDS) }
        } else {
            it
        }
    }

    companion object {
        private const val DELAY_TIME = 1000L
    }


}