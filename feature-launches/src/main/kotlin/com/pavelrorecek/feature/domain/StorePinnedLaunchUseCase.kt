package com.pavelrorecek.feature.domain

import com.pavelrorecek.feature.model.Launch

internal class StorePinnedLaunchUseCase(private val repository: LaunchesRepository) {

    suspend operator fun invoke(launch: Launch) = repository.storePinned(launch)
}
