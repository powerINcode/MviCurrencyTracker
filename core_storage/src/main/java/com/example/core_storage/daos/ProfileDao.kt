package com.example.core_storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.core_storage.models.profile.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
abstract class ProfileDao {

    @Transaction
    @Query("SELECT * FROM PROFILEENTITY ORDER BY id DESC LIMIT 1")
    abstract fun getProfile(): Maybe<ProfileEntity>

    @Insert(onConflict = REPLACE)
    abstract fun saveProfile(profile: ProfileEntity): Completable

    interface Injector {
        fun getProfileDao(): ProfileDao
    }
}