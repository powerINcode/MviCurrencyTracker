package com.example.feature_rate_tracker.impl.domain

import com.example.core.domain.datadelegate.Data
import com.example.feature_rate_tracker_api.data.RateTrackerRepository
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

internal class ObserveCurrencyRatesUseCaseImpl @Inject constructor(
    private val repository: RateTrackerRepository
) : ObserveCurrencyRatesUseCase {

    override fun invoke(params: String): Observable<Data<List<Currency>>> =
        repository.observeLatest(params)

}
