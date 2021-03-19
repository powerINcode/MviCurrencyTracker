package com.example.feature_rate_tracker_impl

import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_impl.delegates.RateDelegate

interface MainScreenContract {
    data class RateTrackerState(
        val currency: RateDelegate.Model,
        val currencies: List<RateDelegate.Model>,
        val error: Boolean,
        val loading: Boolean
    ) {
        companion object {
            val EMPTY =
                RateTrackerState(
                    currency = RateDelegate.Model(
                        id = RATE_ITEM_ID,
                        name = DEFAULT_CURRENCY,
                        amount = DEFAULT_CURRENCY_VALUE,
                        rate = DEFAULT_CURRENCY_RATE
                    ),
                    currencies = emptyList(),
                    loading = false,
                    error = false
                )
        }
    }

    sealed class RateTrackerIntent {
        data class CurrencySelected(val currency: RateDelegate.Model) : RateTrackerIntent()
        data class AmountUpdated(val amount: Double) : RateTrackerIntent()
        object NavigateToInfo : RateTrackerIntent()
    }

    sealed class RateChange : com.example.core.mvi.Change {
        object StartLoading : RateChange()
        object StopLoading : RateChange()
        object Error : RateChange()
        object HideError : RateChange()
        object RecalculateAmounts : RateChange()
        data class SelectNewCurrency(val currency: RateDelegate.Model) : RateChange()
        data class UpdateAmount(val amount: Double) : RateChange()
        data class UpdateRates(val currencies: List<Currency>) : RateChange()
    }

    companion object {
        const val RATE_ITEM_ID = "RATE_ITEM_ID"
        const val DEFAULT_CURRENCY = "EUR"
        const val DEFAULT_CURRENCY_VALUE = 1.0
        const val DEFAULT_CURRENCY_RATE = 1.0
    }
}