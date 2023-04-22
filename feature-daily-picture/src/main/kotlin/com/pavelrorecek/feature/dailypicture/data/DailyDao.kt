package com.pavelrorecek.feature.dailypicture.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
internal interface DailyDao {

    @Query("SELECT * FROM daily")
    fun first(): DailyEo?

    @Upsert
    fun insert(eo: DailyEo)

    @Query("DELETE FROM daily")
    fun deleteAll()
}
