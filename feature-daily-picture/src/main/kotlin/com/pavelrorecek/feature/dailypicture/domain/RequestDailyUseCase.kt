package com.pavelrorecek.feature.dailypicture.domain

internal class RequestDailyUseCase(
    private val repository: DailyPictureRepository,
) {

    suspend operator fun invoke() {
        repository.load()
    }
}
