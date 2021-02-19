package com.example.core_storage.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.core_storage.models.profile.ProfileEntity

@Dao
interface ProfileDao {

    @Query("SELECT * FROM PROFILEENTITY ORDER BY id DESC LIMIT 1")
    suspend fun getProfile(): ProfileEntity?

    @Insert(onConflict = REPLACE)
    suspend fun saveProfile(profile: ProfileEntity)

    interface Injector {
        fun getProfileDao(): ProfileDao
    }
}