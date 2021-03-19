package com.example.feature_rate_tracker_impl

import com.example.core.viewmodel.StateReducer
import com.example.feature_rate_tracker_impl.MainScreenContract.*
import com.example.feature_rate_tracker_impl.delegates.RateDelegate
import javax.inject.Inject

class MainStateReducer @Inject constructor() : StateReducer<RateTrackerState, RateChange> {
    override fun reduce(
        state: RateTrackerState,
        change: RateChange
    ): RateTrackerState {
        return when (change) {
            RateChange.StartLoading -> state.copy(
                loading = true
            )
            is RateChange.StopLoading -> state.copy(
                loading = false
            )
            RateChange.Error -> state.copy(
                error = true
            )
            is RateChange.HideError -> state.copy(
                error = false
            )
            is RateChange.SelectNewCurrency -> {
                state.copy(
                    currency = change.currency,
                    currencies = state.currencies.selectCurrency(change.currency.name)
                )
            }
            is RateChange.UpdateAmount -> {
                state.copy(
                    currency = state.currency.copy(amount = change.amount)
                )
            }
            is RateChange.RecalculateAmounts -> {
                val currencies = state.currencies.map { currency ->
                    currency.copy(
                        amount = calculateAmount(state.currency.amount, currency.rate)
                    )
                }
                state.copy(currencies = currencies)
            }
            is RateChange.UpdateRates -> {
                val currencies = if (state.currencies.size != change.currencies.size) {
                    change.currencies.map {
                        RateDelegate.Model(
                            id = "${Companion.RATE_ITEM_ID}-${it.name}",
                            name = it.name,
                            amount = Companion.DEFAULT_CURRENCY_VALUE,
                            rate = it.rate
                        )
                    }.selectCurrency(change.currencies.first().name)
                } else {
                    state.currencies
                }

                val updatedCurrencies = currencies.map { currency ->
                    val rate = change.currencies.first { it.name == currency.name }.rate
                    currency.copy(rate = rate)
                }
                state.copy(currencies = updatedCurrencies)
            }
        }
    }

    private fun calculateAmount(amount: Double, rate: Double): Double {
        val safeRate = if (rate == 0.0) 1.0 else rate
        return amount.times(safeRate)
    }

    private fun List<RateDelegate.Model>.selectCurrency(currency: String): List<RateDelegate.Model> {
        return this.toMutableList().apply {
            val indexOfFirst = indexOfFirst { it.name == currency }
            val item = removeAt(indexOfFirst)
            add(0, item)
        }
    }
}