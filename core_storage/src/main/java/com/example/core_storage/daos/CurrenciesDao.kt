package com.example.core_storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core_storage.models.currency.CurrencyEntity

@Dao
interface CurrenciesDao {

    @Query("SELECT * FROM currencyentity")
    suspend fun get(): List<CurrencyEntity>?

    @Insert(onConflict = REPLACE)
    suspend fun set(currencies: List<CurrencyEntity>)

    interface Injector {
        fun getCurrencyDao(): CurrenciesDao
    }
}