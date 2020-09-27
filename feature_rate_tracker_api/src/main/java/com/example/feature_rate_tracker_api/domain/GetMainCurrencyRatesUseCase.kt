package com.example.feature_rate_tracker_api.domain

import com.example.core.usecase.MaybeDataUseCase
import com.example.core.usecase.MaybeSimpleDataUseCase
import com.example.core.usecase.MaybeSimpleUseCase
import com.example.core.usecase.ObservableDataUseCase
import com.example.feature_rate_tracker_api.data.models.Currency

interface GetMainCurrencyRatesUseCase : MaybeSimpleUseCase<Currency>