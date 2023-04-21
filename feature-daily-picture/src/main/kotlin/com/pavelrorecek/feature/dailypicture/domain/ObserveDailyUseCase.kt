package com.pavelrorecek.feature.dailypicture.domain

internal class ObserveDailyUseCase(
    private val repository: DailyPictureRepository,
) {

    operator fun invoke() = repository.observe()
}
