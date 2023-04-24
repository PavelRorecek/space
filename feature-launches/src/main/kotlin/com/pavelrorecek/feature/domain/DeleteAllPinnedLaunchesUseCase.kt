package com.pavelrorecek.feature.domain

import com.pavelrorecek.feature.model.Launch

internal class DeleteAllPinnedLaunchesUseCase(private val repository: LaunchesRepository) {

    suspend operator fun invoke() = repository.deleteAllPinned()
}
