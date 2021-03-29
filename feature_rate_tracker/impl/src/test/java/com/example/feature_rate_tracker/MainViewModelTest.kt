package com.example.feature_rate_tracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core.domain.datadelegate.Data
import com.example.core.test.RxJavaTestRule
import com.example.core.test.thenEmit
import com.example.core.test.thenEmitEmpty
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_api.domain.GetMainCurrencyRatesUseCase
import com.example.feature_rate_tracker_api.domain.ObserveAdvertisementUseCase
import com.example.feature_rate_tracker_api.domain.ObserveCurrencyRatesUseCase
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.subjects.PublishSubject
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
    private val getMainCurrency: GetMainCurrencyRatesUseCase = mock()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        whenever(observeCurrencyRates.invoke(any()))
            .thenEmit(Data.Complete(emptyList()))
        whenever(getMainCurrency()).thenEmitEmpty()

        viewModel = MainViewModel(
            rateTrackerStateReducer = rateTrackerStateReducer,
            observeCurrencyRates = observeCurrencyRates,
            getMainCurrency = getMainCurrency,
            observeAdvertisement = observeAdvertisement
        ).apply { infinityLoading = false }

    }

    @Test
    fun `state | initiation`() {
        val defaultCurrency = MainScreenContract.RateTrackerState.EMPTY.currency
        val defaultAmount = MainScreenContract.RateTrackerState.EMPTY.amount
        val testObservable = PublishSubject.create<Data<List<Currency>>>()
        whenever(observeCurrencyRates.invoke(any())).thenReturn(testObservable)

        viewModel.init()

        verify(getMainCurrency).invoke()
        verify(rateTrackerStateReducer).selectCurrency(defaultCurrency, defaultAmount)
        verify(observeCurrencyRates).invoke(defaultCurrency.name)

        testObservable.onNext(Data.Loading(null))
        testObservable.onNext(Data.Complete(emptyList()))

        verify(rateTrackerStateReducer).selectCurrency(defaultCurrency, defaultAmount)
        verify(rateTrackerStateReducer).startLoadingRates()
        verify(rateTrackerStateReducer).ratesLoaded(any())


    }
}