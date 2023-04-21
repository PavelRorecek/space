package com.pavelrorecek.feature.dailypicture.domain

internal class LoadDailyUseCase(
    private val repository: DailyPictureRepository,
) {

    suspend operator fun invoke() {
        repository.load()
    }
}
