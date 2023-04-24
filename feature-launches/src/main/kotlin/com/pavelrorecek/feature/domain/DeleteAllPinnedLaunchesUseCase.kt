package com.pavelrorecek.feature.domain

internal class DeleteAllPinnedLaunchesUseCase(private val repository: LaunchesRepository) {

    suspend operator fun invoke() = repository.deleteAllPinned()
}
