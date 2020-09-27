package com.example.core_storage.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.core_storage.ApplicationDatabase
import com.example.core_storage.daos.CurrenciesDao
import com.example.core_storage.daos.ProfileDao
import com.example.core_storage.migrations.MIGRATION_1_2
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface CoreStorageModule {

    companion object {
        @Singleton
        @Provides
        fun provideDataBase(context: Context): ApplicationDatabase = Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            "rate_tracker_db"
        )
            .addMigrations(
                MIGRATION_1_2
            )
            .build()

        @Singleton
        @Provides
        fun provideCurrencyDao(db: ApplicationDatabase): CurrenciesDao = db.getCurrencyDao()

        @Singleton
        @Provides
        fun provideProfileDao(db: ApplicationDatabase): ProfileDao = db.getProfileDao()
    }
}