package com.pavelrorecek.feature.dailypicture.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DailyEo::class], version = 1)
internal abstract class DailyDatabase : RoomDatabase() {
    abstract fun dailyDao(): DailyDao
}
