package com.example.feature_rate_tracker_impl.data

import android.util.Log
import com.example.core.coroutine.onIo
import com.example.core_data.datadelegate.Data
import com.example.core_data.datadelegate.DataDelegate
import com.example.core_data.datadelegate.DataManager
import com.example.core_storage.daos.CurrenciesDao
import com.example.core_storage.models.currency.CurrencyEntity
import com.example.feature_rate_tracker_api.data.RateTrackerRepository
import com.example.feature_rate_tracker_api.data.models.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

internal class RateTrackerRepositoryImpl @Inject constructor(
    private val service: RateService,
    private val currencyDao: CurrenciesDao
) : RateTrackerRepository {

    private var currencyCache: List<Currency>? = null

    private val delegate = DataManager(object : DataDelegate<String, List<Currency>> {

        override suspend fun getFromMemory(): List<Currency>? = currencyCache

        override suspend fun putToMemory(data: List<Currency>) {
            currencyCache = data
        }

        override suspend fun getFromStorage(): List<Currency>? = currencyDao.get()
            ?.map { entity -> Currency(name = entity.name, rate = entity.rate) }

        override suspend fun putToStorage(data: List<Currency>) {
            data.map { domain ->
                CurrencyEntity(
                    name = domain.name,
                    rate = domain.rate
                )
            }
                .let {
                    currencyDao.set(it)
                }
        }

        override suspend fun getFromNetwork(params: String): List<Currency> {
            Log.e("DOD", "getFromNetwork")
            return service.latest(params)
                .let {
                    listOf(
                        Currency(
                            name = "AUD",
                            rate = it.rates.AUD
                        ),
                        Currency(
                            name = "BGN",
                            rate = it.rates.BGN
                        ),
                        Currency(
                            name = "CAD",
                            rate = it.rates.CAD
                        ),
                        Currency(
                            name = "CNY",
                            rate = it.rates.CNY
                        ),
                        Currency(
                            name = "DKK",
                            rate = it.rates.DKK
                        ),
                        Currency(
                            name = "GBP",
                            rate = it.rates.GBP
                        ),
                        Currency(
                            name = "HKD",
                            rate = it.rates.HKD
                        ),

                        Currency(
                            name = "HRK",
                            rate = it.rates.HRK
                        ),
                        Currency(
                            name = "HUF",
                            rate = it.rates.HUF
                        ),
                        Currency(
                            name = "IDR",
                            rate = it.rates.IDR
                        ),
                        Currency(
                            name = "EUR",
                            rate = it.rates.EUR
                        )
                    ).toMutableList().apply {
                        Collections.swap(this, indexOfFirst { it.name == params }, 0)
                    }
                }
        }

    })

    override suspend fun observeLatest(currency: String): Flow<Data<List<Currency>>> =
        delegate.observe(true, currency)

    override suspend fun getMainCurrency(): Currency? =
        onIo {
            currencyDao.get()
                .let { currencies ->
                    currencies?.firstOrNull()?.let {
                        Currency(
                            name = it.name,
                            rate = it.rate
                        )
                    }

                }
        }
}