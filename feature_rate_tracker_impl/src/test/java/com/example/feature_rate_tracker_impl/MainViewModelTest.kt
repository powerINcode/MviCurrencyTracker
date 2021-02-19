package com.example.feature_rate_tracker_impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core_data.datadelegate.Data
import com.example.core_test.CoroutineTestRule
import com.example.core_test.thenEmit
import com.example.core_test.thenReturnEmpty
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
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

    private val rateTrackerStateReducer: MainStateReducer = mock()
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase = mock()
    private val getMainCurrency: GetMainCurrencyRatesUseCase = mock()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() = runBlocking {
        whenever(
            rateTrackerStateReducer.reduce(
                any(),
                any()
            )
        ).doReturn(MainScreenContract.RateTrackerState.EMPTY)
        whenever(observeCurrencyRates.invoke(any())).thenEmit(Data.Complete(emptyList()))
        whenever(getMainCurrency()).thenReturnEmpty()

        viewModel = MainViewModel(
            reducer = rateTrackerStateReducer,
            observeCurrencyRates = observeCurrencyRates,
            getMainCurrency = getMainCurrency
        ).apply { infinityLoading = false }

    }

    @Test
    fun `state | initiation`() = runBlocking {
        whenever(observeCurrencyRates.invoke(any())).thenEmit(Data.Complete(emptyList()))

        viewModel.init()

        verify(getMainCurrency).invoke()
        verify(observeCurrencyRates).invoke(MainScreenContract.DEFAULT_CURRENCY)

        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.StartLoading))
        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.StopLoading))
        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.UpdateRates(emptyList())))
        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.RecalculateAmounts))
        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.HideError))

        Unit
    }
}