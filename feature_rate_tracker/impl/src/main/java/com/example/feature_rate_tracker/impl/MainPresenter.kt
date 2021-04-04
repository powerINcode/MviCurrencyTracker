package com.example.feature_rate_tracker.impl

import com.example.core.domain.datadelegate.extractContent
import com.example.core.domain.routing.FeatureCommand
import com.example.core.domain.routing.Navigator
import com.example.core.ui.presenter.BasePresenter
import com.example.feature_profile.api.declaration.ProfileFeatureConfig
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerIntent.*
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerState
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val navigator: Navigator
) : BasePresenter<RateTrackerState, MainStateReducer, MainViewModel>() {

    override fun onCreated() {
        super.onCreated()

        viewModel.advertisementFlow
            .collectInScope {
                reducer.addAdvertisements(it)
            }

        intentOf<NavigateToInfo>()
            .collectInScope {
                navigator.navigate(FeatureCommand(ProfileFeatureConfig))
            }

        intentOf<AmountUpdated>()
            .map { it.amount }
            .onEach { reducer.updateAmount(it) }
            .collectInScope()

        intentOf<CurrencySelected>()
            .onStart {
                emit(CurrencySelected(viewModel.getInitialCurrency(), viewModel.getInitialCurrencyAmount()))
            }
            .onEach { reducer.selectCurrency(it.currency, it.amount) }
            .map { it.currency }
            .flatMapLatest { currency ->
                viewModel.observeLatestCurrenciesFor(currency)
                    .extractContent(
                        dropCache = true,
                        onError = { e ->
                            reducer.ratesLoadingError()
                            e
                        },
                        onContentEmpty = {
                            reducer.startLoadingRates()
                        },
                        onContentAvailable = {
                            reducer.ratesLoaded(it)
                        }
                    )
            }
            .collectInScope()
    }
}