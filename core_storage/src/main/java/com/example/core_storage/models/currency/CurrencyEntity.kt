package com.example.core_storage.models.currency

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyEntity(
    @PrimaryKey
    val name: String,
    val rate: Double
)