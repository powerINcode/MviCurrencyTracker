package com.example.feature_rate_tracker_impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core_data.datadelegate.Data
import com.example.core_test.CoroutineTestRule
import com.example.core_test.thenEmit
import com.example.core_test.thenReturnEmpty
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @Rule
    @JvmField
    val rxRule: TestRule = CoroutineTestRule()

    @Rule
    @JvmField
    val liveDataRule: TestRule = InstantTaskExecutorRule()

    private val rateTrackerStateReducer: MainStateReducer = spy(MainStateReducer())
    private val observeAdvertisement: ObserveAdvertisementUseCase = mock()
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase = mock()
    private val getMainCurrency: GetMainCurrencyRatesUseCase = mock()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() = runBlocking {
        whenever(observeCurrencyRates.invoke(any())).thenEmit(Data.Complete(emptyList()))
        whenever(getMainCurrency()).thenReturnEmpty()

        viewModel = MainViewModel(
            reducer = rateTrackerStateReducer,
            observeCurrencyRates = observeCurrencyRates,
            getMainCurrency = getMainCurrency,
            observeAdvertisement = observeAdvertisement
        ).apply { infinityLoading = false }

    }

    @Test
    fun `state | initiation`() = runBlocking {
        val defaultCurrency = MainScreenContract.RateTrackerState.EMPTY.currency
        val defaultAmount = MainScreenContract.RateTrackerState.EMPTY.amount
        whenever(observeCurrencyRates.invoke(any()))
            .thenEmit(Data.Loading(null), Data.Complete(emptyList()))

        viewModel.init()

        verify(getMainCurrency).invoke()
        verify(rateTrackerStateReducer).selectCurrency(defaultCurrency, defaultAmount)
        verify(observeCurrencyRates).invoke(defaultCurrency.name)

        verify(rateTrackerStateReducer).startLoadingRates()
        verify(rateTrackerStateReducer).ratesLoaded(any())
    }
}