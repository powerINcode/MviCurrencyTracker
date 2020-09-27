package com.example.feature_rate_tracker_impl.domain

import com.example.feature_rate_tracker_api.data.RateTrackerRepository
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

internal class GetMainCurrencyRatesUseCaseImpl @Inject constructor(
    private val repository: RateTrackerRepository
): GetMainCurrencyRatesUseCase {
    override fun invoke(params: Unit): Maybe<Currency> = repository.getMainCurrency()

}