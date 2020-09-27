package com.example.feature_rate_tracker_impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core_data.datadelegate.Data
import com.example.core_test.RxJavaTestRule
import com.example.core_test.RxJavaUncaughtErrorRule
import com.example.core_test.thenEmit
import com.example.core_test.thenEmitEmpty
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito

class MainViewModelTest {

    @Rule
    @JvmField
    val rxRule: TestRule = RxJavaTestRule()

    @Rule
    @JvmField
    val liveDataRule: TestRule = InstantTaskExecutorRule()

    private val rateTrackerStateReducer: MainStateReducer = mock()
    private val observeCurrencyRates: ObserveCurrencyRatesUseCase = mock()
    private val getMainCurrency: GetMainCurrencyRatesUseCase = mock()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        whenever(
            rateTrackerStateReducer.reduce(
                any(),
                any()
            )
        ).doReturn(MainScreenContract.RateTrackerState.EMPTY)
        whenever(observeCurrencyRates.invoke(any()))
            .thenEmit(Data.Complete(emptyList()))
        whenever(getMainCurrency()).thenEmitEmpty()

        viewModel = MainViewModel(
            rateTrackerStateReducer = rateTrackerStateReducer,
            observeCurrencyRates = observeCurrencyRates,
            getMainCurrency = getMainCurrency
        ).apply { infinityLoading = false }

    }

    @Test
    fun `state | initiation`() {
        val testObservable = PublishSubject.create<Data<List<Currency>>>()
        whenever(observeCurrencyRates.invoke(any())).thenReturn(testObservable)

        viewModel.init()

        verify(getMainCurrency).invoke()
        verify(observeCurrencyRates).invoke(MainScreenContract.DEFAULT_CURRENCY)

        testObservable.onNext(Data.Complete(emptyList()))

        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.StartLoading))
        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.StopLoading))
        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.UpdateRates(emptyList())))
        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.RecalculateAmounts))
        verify(rateTrackerStateReducer).reduce(any(), eq(MainScreenContract.RateChange.HideError))


    }
}