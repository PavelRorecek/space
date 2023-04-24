package com.pavelrorecek.feature.domain

internal class ObserveLaunchesUseCase(private val repository: LaunchesRepository) {

    operator fun invoke() = repository.observe()
}
