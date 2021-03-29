package com.example.feature_rate_tracker.impl.data

import com.example.feature_rate_tracker.impl.data.models.response.CurrenciesDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RateService {
    @GET("latest")
    fun latest(@Query("base") currency: String): Single<CurrenciesDto>
}