package com.pavelrorecek.feature.domain

internal class ObservePinnedLaunchesUseCase(private val repository: LaunchesRepository) {

    operator fun invoke() = repository.observePinned()
}
