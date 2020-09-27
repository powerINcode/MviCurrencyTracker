package com.example.feature_rate_tracker_impl.data.models.response

import com.example.feature_rate_tracker_impl.data.models.response.CurrenciesRatesDto

internal data class CurrenciesDto(
    val base : String,
    val rates : CurrenciesRatesDto
)