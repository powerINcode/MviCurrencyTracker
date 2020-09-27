package com.example.feature_rate_tracker_impl.di

import com.example.core.di.scopes.FeatureScope
import com.example.feature_rate_tracker_api.data.RateTrackerRepository
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_impl.data.RateService
import com.example.feature_rate_tracker_impl.data.RateTrackerRepositoryImpl
import com.example.feature_rate_tracker_impl.domain.ObserveCurrencyRatesUseCaseImpl
import com.example.feature_rate_tracker_impl.domain.GetMainCurrencyRatesUseCaseImpl
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

    companion object {
        @FeatureScope
        @Provides
        fun provideRateService(retrofit: Retrofit): RateService = retrofit.create(RateService::class.java)
    }


}