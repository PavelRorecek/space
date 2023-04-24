package com.pavelrorecek.feature.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface PinnedLaunchesDao {

    @Query("SELECT * FROM pinnedLaunch")
    fun observeAll(): Flow<List<PinnedLaunchEo>>

    @Query("SELECT * FROM pinnedLaunch")
    fun loadAll(): List<PinnedLaunchEo>

    @Upsert
    fun store(eo: PinnedLaunchEo)

    @Query("DELETE FROM pinnedLaunch WHERE id = :id")
    fun delete(id: String)

    @Query("DELETE FROM pinnedLaunch")
    fun deleteAll()
}
