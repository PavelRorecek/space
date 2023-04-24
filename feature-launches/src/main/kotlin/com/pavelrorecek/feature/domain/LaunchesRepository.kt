package com.pavelrorecek.feature.domain

import com.pavelrorecek.feature.model.Launch
import kotlinx.coroutines.flow.Flow

internal interface LaunchesRepository {
    suspend fun request()
    fun observe(): Flow<LaunchesResult>

    sealed class LaunchesResult {
        object Loading : LaunchesResult()
        object Error : LaunchesResult()
        data class Loaded(val launches: List<Launch>) : LaunchesResult()
    }
}
