package com.pavelrorecek.feature.dailypicture.domain

import com.pavelrorecek.feature.dailypicture.model.Daily
import kotlinx.coroutines.flow.Flow

internal interface DailyPictureRepository {
    suspend fun load()
    fun observe(): Flow<DailyResult>

    sealed class DailyResult {
        data class Loaded(val daily: Daily) : DailyResult()
        object Loading : DailyResult()
        object Error : DailyResult()
    }
}
