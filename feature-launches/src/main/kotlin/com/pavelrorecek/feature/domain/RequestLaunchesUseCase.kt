package com.pavelrorecek.feature.domain

internal class RequestLaunchesUseCase(private val repository: LaunchesRepository) {

    suspend operator fun invoke() {
        repository.request()
    }
}
