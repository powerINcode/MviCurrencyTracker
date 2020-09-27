package com.example.core_storage.models.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val first: String,
    val second: String,
    val last: String
)