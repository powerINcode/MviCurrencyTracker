package com.example.feature_rate_tracker.impl.di

import com.example.core.di.scopes.FeatureScope
import com.example.feature_rate_tracker.impl.data.RateService
import com.example.feature_rate_tracker.impl.data.RateTrackerRepositoryImpl
import com.example.feature_rate_tracker.impl.domain.GetMainCurrencyRatesUseCaseImpl
import com.example.feature_rate_tracker.impl.domain.ObserveAdvertisementUseCaseImpl
import com.example.feature_rate_tracker.impl.domain.ObserveCurrencyRatesUseCaseImpl
import com.example.feature_rate_tracker_api.data.RateTrackerRepository
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface RateTrackerFeatureModule {
    @FeatureScope
    @Binds
    fun bindRateTrackerRepo(repository: RateTrackerRepositoryImpl): RateTrackerRepository

    @FeatureScope
    @Binds
    fun bindGetCurrencyRatesUseCase(usecase: ObserveCurrencyRatesUseCaseImpl): ObserveCurrencyRatesUseCase

    @FeatureScope
    @Binds
    fun bindGetMainCurrencyRatesUseCase(usecase: GetMainCurrencyRatesUseCaseImpl): GetMainCurrencyRatesUseCase

    @FeatureScope
    @Binds
    fun bindObserveAdvertisementUseCase(usecase: ObserveAdvertisementUseCaseImpl): ObserveAdvertisementUseCase

    companion object {
        @FeatureScope
        @Provides
        fun provideRateService(retrofit: Retrofit): RateService = retrofit.create(RateService::class.java)
    }


}