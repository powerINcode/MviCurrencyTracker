package com.example.feature_rate_tracker_api

import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase

interface RateTrackerApi {
    fun getGetCurrencyRatesUseCase(): ObserveCurrencyRatesUseCase
    fun getGetMainCurrencyRatesUseCase(): GetMainCurrencyRatesUseCase
}