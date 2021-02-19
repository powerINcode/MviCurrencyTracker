package com.example.feature_rate_tracker_api.domain

import com.example.core.usecase.*
import com.example.feature_rate_tracker_api.data.models.Currency

interface GetMainCurrencyRatesUseCase : SimpleUseCase<Currency?>