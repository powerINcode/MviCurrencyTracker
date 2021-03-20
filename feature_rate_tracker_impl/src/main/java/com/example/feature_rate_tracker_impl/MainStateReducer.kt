package com.example.feature_rate_tracker_impl

import com.example.core.activity.recyclerview.RecyclerViewDelegate
import com.example.core.viewmodel.StateReducer
import com.example.feature_rate_tracker_api.data.models.Advertisement
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_impl.MainScreenContract.RateTrackerState
import com.example.feature_rate_tracker_impl.delegates.AdvertisementDelegate
import com.example.feature_rate_tracker_impl.delegates.RateDelegate
import javax.inject.Inject

class MainStateReducer @Inject constructor() : StateReducer<RateTrackerState>(RateTrackerState.EMPTY) {

    fun startLoadingRates() {
        state.copy(loading = true).commit()
    }

    fun ratesLoadingError() {
        state.copy(loading = false, error = true).commit()
    }

    fun ratesLoaded(newCurrencies: List<Currency>) {
        state.copy(loading = false).commit()

        val updatedCurrencies = when {
            state.currencies.size == 1 -> {
                newCurrencies
            }
            else -> state.currencies.map { currency ->
                val rate = newCurrencies.first { it.name == currency.name }.rate
                currency.copy(rate = rate)
            }
        }

        val currenciesDelegates = updatedCurrencies.map(::mapCurrencyToDelegate)
            .map { model ->
                val safeRate = if (model.rate == 0.0) 1.0 else model.rate
                val amount = state.amount.times(safeRate)
                model.copy(amount = amount)
            }

        val advertisementsDelegates = state.advertisements.map(::mapAdvertisementToDelegate)


        state.copy(
            currencies = updatedCurrencies,
            screenItems = mixCurrenciesWithAdvertisements(currenciesDelegates, advertisementsDelegates)
        ).commit()

    }

    fun selectCurrency(currency: Currency, amount: Double) {
        state.copy(
            currency = currency,
            amount = amount,
            currencies = state.currencies.moveTop(currency)
        ).commit()
    }

    fun updateAmount(amount: Double) {
        state.copy(amount = amount).commit()
        ratesLoaded(state.currencies)
    }

    fun addAdvertisements(advertisements: List<Advertisement>) {
        state.copy(advertisements = advertisements).commit()
    }

    private fun mixCurrenciesWithAdvertisements(
        currenciesDelegates: List<RateDelegate.Model>,
        advertisementsDelegates: List<AdvertisementDelegate.Model>
    ): List<RecyclerViewDelegate.Model> {
        return if (advertisementsDelegates.isNotEmpty()) {
            val step = 1.coerceAtLeast(currenciesDelegates.size / advertisementsDelegates.size)
            currenciesDelegates.toMutableList<RecyclerViewDelegate.Model>().apply {
                for (i in 1..advertisementsDelegates.size) {
                    add(i * step + i - 1, advertisementsDelegates[i - 1])
                }
            }
        } else {
            currenciesDelegates
        }
    }

    private fun mapCurrencyToDelegate(currency: Currency) = RateDelegate.Model(
        id = "${RATE_ITEM_ID}-${currency.name}",
        name = currency.name,
        amount = 1.0,
        rate = currency.rate,
        extra = currency
    )

    private fun mapAdvertisementToDelegate(advertisement: Advertisement) = AdvertisementDelegate.Model(
        id = "${ADVERTISEMENT_ITEM_ID}-${advertisement.title}",
        title = advertisement.title
    )

    private fun List<Currency>.moveTop(currency: Currency): List<Currency> {
        return this.toMutableList().apply {
            val item = if (this.contains(currency)) {
                val indexOfFirst = indexOf(currency)
                removeAt(indexOfFirst)
            } else {
                currency
            }

            add(0, item)
        }
    }

    companion object {
        const val RATE_ITEM_ID = "RATE_ITEM_ID"
        const val ADVERTISEMENT_ITEM_ID = "ADVERTISEMENT_ITEM_ID"
    }
}