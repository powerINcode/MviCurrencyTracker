package com.example.feature_rate_tracker.impl.di

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
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RateTrackerFeatureModule {
    @Singleton
    @Binds
    fun bindRateTrackerRepo(repository: RateTrackerRepositoryImpl): RateTrackerRepository

    @Singleton
    @Binds
    fun bindGetCurrencyRatesUseCase(usecase: ObserveCurrencyRatesUseCaseImpl): ObserveCurrencyRatesUseCase

    @Singleton
    @Binds
    fun bindGetMainCurrencyRatesUseCase(usecase: GetMainCurrencyRatesUseCaseImpl): GetMainCurrencyRatesUseCase

    @Singleton
    @Binds
    fun bindObserveAdvertisementUseCase(usecase: ObserveAdvertisementUseCaseImpl): ObserveAdvertisementUseCase

    companion object {
        @Singleton
        @Provides
        fun provideRateService(retrofit: Retrofit): RateService = retrofit.create(RateService::class.java)
    }


}