package com.example.feature_rate_tracker_impl.data

import com.example.feature_rate_tracker_impl.data.models.response.CurrenciesDto
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RateService {
    @GET("latest")
    suspend fun latest(@Query("base") currency: String): CurrenciesDto
}