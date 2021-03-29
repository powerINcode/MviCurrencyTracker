package com.example.feature_rate_tracker_api.domain

import com.example.core.domain.usecase.MaybeSimpleUseCase
import com.example.feature_rate_tracker_api.data.models.Currency

interface GetMainCurrencyRatesUseCase : MaybeSimpleUseCase<Currency>