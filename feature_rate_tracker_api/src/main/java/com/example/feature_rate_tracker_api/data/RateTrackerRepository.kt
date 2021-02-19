package com.example.feature_rate_tracker_api.data

import com.example.core_data.datadelegate.Data
import com.example.feature_rate_tracker_api.data.models.Currency
import kotlinx.coroutines.flow.Flow

interface RateTrackerRepository {
    suspend fun observeLatest(currency: String) : Flow<Data<List<Currency>>>
    suspend fun getMainCurrency(): Currency?
}