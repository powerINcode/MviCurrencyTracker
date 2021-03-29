package com.example.feature_rate_tracker.impl.domain

import com.example.core.domain.datadelegate.Data
import com.example.core.domain.datadelegate.asCompleteData
import com.example.feature_rate_tracker_api.data.models.Advertisement
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class ObserveAdvertisementUseCaseImpl @Inject constructor() : ObserveAdvertisementUseCase {
    override suspend fun invoke(params: Unit): Flow<Data<List<Advertisement>>> {
        return flowOf(
            listOf(
                Advertisement("Tinkoff"),
                Advertisement("Axe"),
                Advertisement("Gillette")
            ).asCompleteData()
        ).onStart { delay(3000) }
    }

}