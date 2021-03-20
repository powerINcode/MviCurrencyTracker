package com.example.feature_rate_tracker_api.data

import com.example.core.data.datadelegate.Data
import com.example.feature_rate_tracker_api.data.models.Currency
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

interface RateTrackerRepository {
    fun observeLatest(currency: String) : Observable<Data<List<Currency>>>
    fun getMainCurrency(): Maybe<Currency>
}