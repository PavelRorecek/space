package com.pavelrorecek.feature.domain

import com.pavelrorecek.feature.model.Launch
import kotlinx.coroutines.flow.Flow

internal interface LaunchesRepository {
    suspend fun request()
    fun observe(): Flow<LaunchesResult>
    fun observePinned(): Flow<List<Launch>>
    suspend fun storePinned(launch: Launch)
    suspend fun deletePinned(launch: Launch.Id)
    suspend fun deleteAllPinned()

    sealed class LaunchesResult {
        object Loading : LaunchesResult()
        object Error : LaunchesResult()
        data class Loaded(val launches: List<Launch>) : LaunchesResult()
    }
}
