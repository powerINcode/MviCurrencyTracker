package com.example.feature_rate_tracker_impl.domain

import com.example.core_data.datadelegate.Data
import com.example.core_data.datadelegate.asCompleteData
import com.example.feature_rate_tracker_api.data.models.Advertisement
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class ObserveAdvertisementUseCaseImpl @Inject constructor(): ObserveAdvertisementUseCase {
    override fun invoke(params: Unit): Observable<Data<List<Advertisement>>> {
        return Observable.just((listOf(Advertisement("Tinkoff"), Advertisement("Axe")).asCompleteData()))
    }
}