package com.example.core.storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.core.storage.models.profile.ProfileEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

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