package com.example.feature_rate_tracker_impl.data

import android.util.Log
import com.example.core_data.datadelegate.Data
import com.example.core_data.datadelegate.DataDelegate
import com.example.core_data.datadelegate.DataManager
import com.example.core_storage.daos.CurrenciesDao
import com.example.core_storage.models.currency.CurrencyEntity
import com.example.feature_rate_tracker_api.data.RateTrackerRepository
import com.example.feature_rate_tracker_api.data.models.Currency
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

internal class RateTrackerRepositoryImpl @Inject constructor(
    private val service: RateService,
    private val currencyDao: CurrenciesDao
) : RateTrackerRepository {

    private var currencyCache: List<Currency>? = null

    private val delegate = DataManager(object : DataDelegate<String, List<Currency>> {

        override fun getFromMemory(): Maybe<List<Currency>> = Maybe.fromCallable { currencyCache }

        override fun putToMemory(data: List<Currency>): Completable = Completable.fromAction {
            currencyCache = data
        }

        override fun getFromStorage(): Maybe<List<Currency>> = currencyDao.get()
            .map { dao -> dao.map { entity -> Currency(name = entity.name, rate = entity.rate) } }

        override fun putToStorage(data: List<Currency>): Completable =
            data.map { domain ->
                CurrencyEntity(
                    name = domain.name,
                    rate = domain.rate
                )
            }
                .let {
                    currencyDao.set(it)
                }

        override fun getFromNetwork(params: String): Single<List<Currency>> {
            Log.e("DOD", "getFromNetwork")
            return service.latest(params)
                .map {
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

    override fun observeLatest(currency: String): Observable<Data<List<Currency>>> =
        delegate.observe(true, currency)

    override fun getMainCurrency(): Maybe<Currency> =
        currencyDao.get()
            .flatMap {
                if (it.isEmpty()) {
                    Maybe.empty()
                } else {
                    Maybe.just(it.first())
                }
            }.map {
                Currency(
                    name = it.name,
                    rate = it.rate
                )
            }
            .subscribeOn(Schedulers.io())
}