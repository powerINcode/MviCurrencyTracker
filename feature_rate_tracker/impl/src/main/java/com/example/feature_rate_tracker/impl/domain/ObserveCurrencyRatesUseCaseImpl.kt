package com.example.feature_rate_tracker.impl.domain

import com.example.core.domain.datadelegate.Data
import com.example.feature_rate_tracker_api.data.RateTrackerRepository
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveCurrencyRatesUseCaseImpl @Inject constructor(
    private val repository: RateTrackerRepository
) : ObserveCurrencyRatesUseCase {

    override suspend fun invoke(params: String): Flow<Data<List<Currency>>> =
        repository.observeLatest(params)

}
