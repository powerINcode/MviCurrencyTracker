package com.example.feature_rate_tracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core.domain.datadelegate.Data
import com.example.core.test.RxJavaTestRule
import com.example.core.test.thenEmit
import com.example.core.test.thenEmitEmpty
import com.example.feature_rate_tracker.impl.MainScreenContract
import com.example.feature_rate_tracker.impl.MainStateReducer
import com.example.feature_rate_tracker.impl.MainViewModel
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @Rule
    @JvmField
    val rxRule: TestRule = RxJavaTestRule()

    @Rule
    @JvmField
    val liveDataRule: TestRule = InstantTaskExecutorRule()

    private val rateTrackerStateReducer: MainStateReducer = spy(MainStateReducer())
    private val observeAdvertisement: ObserveAdvertisementUseCase = mock()
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase = mock()
    private val getMainCurrencyUseCase: GetMainCurrencyRatesUseCase = mock()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        whenever(observeCurrencyRates.invoke(any()))
            .thenEmit(Data.Complete(emptyList()))
        whenever(getMainCurrencyUseCase()).thenEmitEmpty()

        viewModel = MainViewModel(
            rateTrackerStateReducer = rateTrackerStateReducer,
            observeCurrencyRates = observeCurrencyRates,
            getMainCurrencyUseCase = getMainCurrencyUseCase,
            observeAdvertisement = observeAdvertisement
        ).apply { infinityLoading = false }

    }

    @Test
    fun testGetInitialCurrency() {
        viewModel.getInitialCurrency()

        verify(getMainCurrencyUseCase).invoke()
    }

    @Test
    fun testGetInitialCurrencyAmount() {
        assertEquals(viewModel.getInitialCurrencyAmount(), MainScreenContract.RateTrackerState.EMPTY.amount)
    }

    @Test
    fun `state | initiation`() {
        whenever(observeAdvertisement.invoke())
            .thenEmit(Data.Complete(emptyList()))

        viewModel.init()

        verify(observeAdvertisement).invoke()
    }
}