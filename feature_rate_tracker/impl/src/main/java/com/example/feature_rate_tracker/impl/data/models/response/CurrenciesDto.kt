package com.example.feature_rate_tracker.impl.data.models.response

internal data class CurrenciesDto(
    val base : String,
    val rates : CurrenciesRatesDto
)