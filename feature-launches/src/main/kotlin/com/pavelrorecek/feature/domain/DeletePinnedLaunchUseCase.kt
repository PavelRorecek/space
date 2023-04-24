package com.pavelrorecek.feature.domain

import com.pavelrorecek.feature.model.Launch

internal class DeletePinnedLaunchUseCase(private val repository: LaunchesRepository) {

    suspend operator fun invoke(id: Launch.Id) = repository.deletePinned(id)
}
