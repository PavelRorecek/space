package com.pavelrorecek.feature.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PinnedLaunchEo::class], version = 1)
internal abstract class PinnedLaunchesDatabase : RoomDatabase() {
    abstract fun pinnedLaunchesDao(): PinnedLaunchesDao
}

