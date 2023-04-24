package com.pavelrorecek.feature.data

import com.pavelrorecek.feature.domain.LaunchesRepository
import com.pavelrorecek.feature.domain.LaunchesRepository.LaunchesResult
import com.pavelrorecek.feature.model.Launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class LaunchesRepositoryImpl(
    private val api: LaunchesApi,
) : LaunchesRepository {

    private val launches: MutableStateFlow<LaunchesResult?> = MutableStateFlow(null)

    override suspend fun request() {
        launches.value = LaunchesResult.Loading

        val dtos = with(Dispatchers.IO) {
            runCatching { api.launches() }.getOrNull()
        }

        launches.value = dtos
            ?.map { dto ->
                Launch(
                    id = dto.id.let(Launch::Id),
                    name = dto.name,
                    livestreamUrl = dto.links?.webcast,
                    wikipediaUrl = dto.links?.wikipedia,
                )
            }
            ?.let(LaunchesResult::Loaded)
            ?: LaunchesResult.Error
    }

    override fun observe(): Flow<LaunchesResult> = launches.filterNotNull()
}
