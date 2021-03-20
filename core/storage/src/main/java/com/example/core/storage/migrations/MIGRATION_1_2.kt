package com.example.core.storage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS ProfileEntity (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `first` TEXT NOT NULL, `second` TEXT NOT NULL, `last` TEXT NOT NULL)")
    }
}