package com.example.core.storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.storage.models.currency.CurrencyEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
abstract class CurrenciesDao {

    @Query("SELECT * FROM currencyentity")
    abstract fun get(): Maybe<List<CurrencyEntity>>

    @Insert(onConflict = REPLACE)
    abstract fun set(currencies: List<CurrencyEntity>): Completable

    interface Injector {
        fun getCurrencyDao(): CurrenciesDao
    }
}