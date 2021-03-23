package com.example.feature_rate_tracker_api.domain

import com.example.core.domain.usecase.ObservableDataUseCase
import com.example.feature_rate_tracker_api.data.models.Currency

interface ObserveCurrencyRatesUseCase : ObservableDataUseCase<String, List<Currency>>