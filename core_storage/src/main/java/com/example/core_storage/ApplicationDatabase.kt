package com.example.core_storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core_storage.daos.CurrenciesDao
import com.example.core_storage.daos.ProfileDao
import com.example.core_storage.models.currency.CurrencyEntity
import com.example.core_storage.models.profile.ProfileEntity

@Database(
    version = 2,
    entities = [
        CurrencyEntity::class,
        ProfileEntity::class
    ]
)
abstract class ApplicationDatabase : RoomDatabase(),
    CurrenciesDao.Injector,
    ProfileDao.Injector