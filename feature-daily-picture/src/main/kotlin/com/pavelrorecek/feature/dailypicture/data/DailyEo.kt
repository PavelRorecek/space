package com.pavelrorecek.feature.dailypicture.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily")
internal data class DailyEo(
    @PrimaryKey val title: String,
    val date: Long,
    val explanation: String,
    val imagePath: String?,
)
