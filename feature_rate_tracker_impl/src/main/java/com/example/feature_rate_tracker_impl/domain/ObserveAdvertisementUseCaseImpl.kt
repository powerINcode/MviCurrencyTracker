package com.example.feature_rate_tracker_impl.domain

import com.example.core_data.datadelegate.Data
import com.example.core_data.datadelegate.asCompleteData
import com.example.feature_rate_tracker_api.data.models.Advertisement
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class ObserveAdvertisementUseCaseImpl @Inject constructor(): ObserveAdvertisementUseCase {
    override suspend fun invoke(params: Unit): Flow<Data<List<Advertisement>>> {
        return flowOf(listOf(Advertisement("Tinkoff"), Advertisement("Axe")).asCompleteData())
    }

}