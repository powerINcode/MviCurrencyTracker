package com.example.feature_rate_tracker_impl

import com.example.core.ui.recyclerview.RecyclerViewDelegate
import com.example.feature_rate_tracker_api.data.models.Advertisement
import com.example.feature_rate_tracker_api.data.models.Currency

interface MainScreenContract {
    data class RateTrackerState(
        val currency: Currency,
        val amount: Double,
        val currencies: List<Currency>,
        val advertisements: List<Advertisement>,
        val error: Boolean,
        val loading: Boolean,
        val screenItems: List<RecyclerViewDelegate.Model>
    ) {
        companion object {
            val EMPTY =
                RateTrackerState(
                    currency = Currency(
                        name = "EUR",
                        rate = 1.0
                    ),
                    amount = 1.0,
                    currencies = emptyList(),
                    advertisements = emptyList(),
                    loading = false,
                    error = false,
                    screenItems = emptyList()
                )
        }
    }

    sealed class RateTrackerIntent {
        data class CurrencySelected(val currency: Currency, val amount: Double) : RateTrackerIntent()
        data class AmountUpdated(val amount: Double) : RateTrackerIntent()
        object NavigateToInfo : RateTrackerIntent()
    }
}