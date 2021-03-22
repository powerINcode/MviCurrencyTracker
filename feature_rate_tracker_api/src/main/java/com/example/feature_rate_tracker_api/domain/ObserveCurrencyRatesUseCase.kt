package com.example.feature_rate_tracker_api.domain

import com.example.core.domain.usecase.FlowDataUseCase
import com.example.feature_rate_tracker_api.data.models.Currency

interface ObserveCurrencyRatesUseCase : FlowDataUseCase<String, List<Currency>>