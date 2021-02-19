package com.example.feature_rate_tracker_impl.domain

import com.example.feature_rate_tracker_api.data.RateTrackerRepository
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import javax.inject.Inject

internal class GetMainCurrencyRatesUseCaseImpl @Inject constructor(
    private val repository: RateTrackerRepository
) : GetMainCurrencyRatesUseCase {
    override suspend fun invoke(params: Unit): Currency? = repository.getMainCurrency()

}