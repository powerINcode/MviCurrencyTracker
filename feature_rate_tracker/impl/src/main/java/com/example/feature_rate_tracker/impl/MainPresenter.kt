package com.example.feature_rate_tracker.impl

import com.example.core.domain.datadelegate.extractContent
import com.example.core.domain.routing.FeatureCommand
import com.example.core.domain.routing.Navigator
import com.example.core.ui.presenter.BasePresenter
import com.example.feature_profile.api.declaration.ProfileFeatureConfig
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerIntent.*
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerState
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val navigator: Navigator
) : BasePresenter<RateTrackerState, MainStateReducer, MainViewModel>() {

    override fun onCreated() {
        super.onCreated()

        viewModel.advertisementFlow
            .subscribeTillAttach {
                reducer.addAdvertisements(it)
            }

        intentOf<NavigateToInfo>()
            .subscribeTillAttach {
                navigator.navigate(FeatureCommand(ProfileFeatureConfig))
            }

        intentOf<AmountUpdated>()
            .map { it.amount }
            .doOnNext { reducer.updateAmount(it) }
            .subscribeTillAttach()

        intentOf<CurrencySelected>()
            .startWith(
                viewModel.getInitialCurrency()
                    .map { currency -> CurrencySelected(currency, viewModel.getInitialCurrencyAmount()) }
            )
            .doOnNext { reducer.selectCurrency(it.currency, it.amount) }
            .map { it.currency }
            .switchMap { currency ->
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
            .subscribeTillAttach()
    }
}