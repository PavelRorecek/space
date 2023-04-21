package com.pavelrorecek.feature.dailypicture.domain

import com.pavelrorecek.feature.dailypicture.model.Daily
import kotlinx.coroutines.flow.Flow

internal interface DailyPictureRepository {
    suspend fun load()
    fun observe(): Flow<Daily>
}
