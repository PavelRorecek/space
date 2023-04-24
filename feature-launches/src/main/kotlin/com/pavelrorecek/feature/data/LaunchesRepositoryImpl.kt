package com.pavelrorecek.feature.data

import com.pavelrorecek.feature.domain.LaunchesRepository
import com.pavelrorecek.feature.domain.LaunchesRepository.LaunchesResult
import com.pavelrorecek.feature.model.Launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant

internal class LaunchesRepositoryImpl(
    private val api: LaunchesApi,
    private val pinnedDao: PinnedLaunchesDao,
) : LaunchesRepository {

    private val launches: MutableStateFlow<LaunchesResult?> = MutableStateFlow(null)

    override suspend fun request() {
        launches.value = LaunchesResult.Loading

        val dtos = with(Dispatchers.IO) {
            runCatching { api.launches() }.getOrNull()
        }

        launches.value = dtos
            ?.filter { it.upcoming }
            ?.map { dto ->
                Launch(
                    id = dto.id.let(Launch::Id),
                    name = dto.name,
                    livestreamUrl = dto.links?.webcast,
                    wikipediaUrl = dto.links?.wikipedia,
                    launch = Instant.fromEpochSeconds(dto.dateUnix),
                    isPinned = isPinned(dto.id),
                )
            }
            ?.sortedByDescending { it.launch }
            ?.let(LaunchesResult::Loaded)
            ?: LaunchesResult.Error
    }

    override fun observe(): Flow<LaunchesResult> = launches.filterNotNull()

    override fun observePinned() =
        pinnedDao.observeAll().map { it.map(::toDomain) }.flowOn(Dispatchers.IO)

    private fun toDomain(eo: PinnedLaunchEo) = Launch(
        id = eo.id.let(Launch::Id),
        name = eo.name,
        livestreamUrl = eo.livestreamUrl,
        wikipediaUrl = eo.wikipediaUrl,
        launch = Instant.fromEpochSeconds(eo.launch),
        isPinned = true,
    )

    private fun toEntity(model: Launch) = PinnedLaunchEo(
        id = model.id.value,
        name = model.name,
        livestreamUrl = model.livestreamUrl,
        wikipediaUrl = model.wikipediaUrl,
        launch = model.launch.epochSeconds,
    )

    private suspend fun isPinned(id: String) = withContext(Dispatchers.IO) {
        pinnedDao.loadAll().map { it.id }.contains(id)
    }

    override suspend fun storePinned(launch: Launch) {
        withContext(Dispatchers.IO) {
            pinnedDao.store(toEntity(launch))
        }
        refreshPinned()
    }

    override suspend fun deletePinned(id: Launch.Id) {
        withContext(Dispatchers.IO) {
            pinnedDao.delete(id.value)
        }
        refreshPinned()
    }

    override suspend fun deleteAllPinned() {
        withContext(Dispatchers.IO) {
            pinnedDao.deleteAll()
        }
        refreshPinned()
    }

    // Updates launches so that the ones that are pinned are loaded from persistence instead
    private suspend fun refreshPinned() {
        val currentResult = launches.value
        val pinnedLaunches = withContext(Dispatchers.IO) { pinnedDao.loadAll().map(::toDomain) }

        if (currentResult is LaunchesResult.Loaded) {
            launches.value = currentResult.launches.map { launch ->
                pinnedLaunches.firstOrNull { it.id == launch.id } ?: launch.copy(isPinned = false)
            }.let(LaunchesResult::Loaded)
        }
    }
}
