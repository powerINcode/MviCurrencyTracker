package com.example.feature_rate_tracker

import com.example.core.domain.datadelegate.Data
import com.example.core.domain.routing.Navigator
import com.example.core.test.CoroutineTestRule
import com.example.core.test.thenEmit
import com.example.feature_rate_tracker.impl.MainPresenter
import com.example.feature_rate_tracker.impl.MainScreenContract
import com.example.feature_rate_tracker.impl.MainStateReducer
import com.example.feature_rate_tracker.impl.MainViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainPresenterTest {
    @Rule
    @JvmField
    val rxRule = CoroutineTestRule()

    private val navigator: Navigator = mock()
    private val reducer: MainStateReducer = mock()
    private val viewModel: MainViewModel = mock()

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        whenever(viewModel.advertisementFlow).thenEmit(emptyList())
        whenever(viewModel.reducer).thenReturn(reducer)
        presenter = MainPresenter(
            navigator = navigator
        )

        presenter.onCreate(rxRule, viewModel)
    }

    @Test
    fun `state | initiation`() = runBlocking {
        val defaultCurrency = MainScreenContract.RateTrackerState.EMPTY.currency
        val defaultAmount = MainScreenContract.RateTrackerState.EMPTY.amount
        whenever(viewModel.getInitialCurrency()).thenReturn(defaultCurrency)
        whenever(viewModel.getInitialCurrencyAmount()).thenReturn(defaultAmount)
        whenever(viewModel.observeLatestCurrenciesFor(defaultCurrency))
            .thenEmit(Data.Loading(), Data.Complete(listOf(defaultCurrency)))

        presenter.onCreated()

        verify(reducer).selectCurrency(defaultCurrency, defaultAmount)

        verify(reducer).startLoadingRates()
        verify(reducer).ratesLoaded(any())
    }
}