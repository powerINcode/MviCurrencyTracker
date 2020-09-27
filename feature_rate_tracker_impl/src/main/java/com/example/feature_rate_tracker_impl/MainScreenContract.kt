package com.example.feature_rate_tracker_impl

import com.example.feature_rate_tracker_api.data.models.Currency

interface MainScreenContract {
    data class RateTrackerState(
        val currency: ScreenCurrency,
        val currencies: List<ScreenCurrency>,
        val error: Boolean,
        val loading: Boolean
    ) {
        companion object {
            val EMPTY =
                RateTrackerState(
                    currency = ScreenCurrency(
                        DEFAULT_CURRENCY,
                        DEFAULT_CURRENCY_VALUE,
                        DEFAULT_CURRENCY_RATE
                    ),
                    currencies = emptyList(),
                    loading = false,
                    error = false
                )
        }
    }

    sealed class RateTrackerIntent {
        data class CurrencySelected(val currency: ScreenCurrency) : RateTrackerIntent()
        data class AmountUpdated(val amount: Double) : RateTrackerIntent()
        object NavigateToInfo : RateTrackerIntent()
    }

    sealed class RateChange : com.example.core.mvi.Change {
        object StartLoading : RateChange()
        object StopLoading : RateChange()
        object Error : RateChange()
        object HideError : RateChange()
        object RecalculateAmounts : RateChange()
        data class SelectNewCurrency(val currency: ScreenCurrency) : RateChange()
        data class UpdateAmount(val amount: Double) : RateChange()
        data class UpdateRates(val currencies: List<Currency>) : RateChange()
    }

    data class ScreenCurrency(val name: String, val amount: Double, val rate: Double)

    companion object {
        const val DEFAULT_CURRENCY = "EUR"
        const val DEFAULT_CURRENCY_VALUE = 1.0
        const val DEFAULT_CURRENCY_RATE = 1.0
    }
}