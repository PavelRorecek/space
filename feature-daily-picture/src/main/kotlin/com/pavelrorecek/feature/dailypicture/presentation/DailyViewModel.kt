package com.pavelrorecek.feature.dailypicture.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelrorecek.feature.dailypicture.domain.DailyPictureRepository
import com.pavelrorecek.feature.dailypicture.domain.ObserveDailyUseCase
import com.pavelrorecek.feature.dailypicture.domain.RequestDailyUseCase
import com.pavelrorecek.feature.dailypicture.model.Daily.Image
import com.pavelrorecek.feature.dailypicture.presentation.DailyViewModel.State.ExplanationState
import com.pavelrorecek.feature.dailypicture.presentation.DailyViewModel.State.ImageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

internal class DailyViewModel(
    private val requestDaily: RequestDailyUseCase,
    private val observeDaily: ObserveDailyUseCase,
    strings: DailyStrings,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State(
            today = strings.today(),
            explanationTitle = strings.explanation(),
        ),
    )
    val state: StateFlow<State> = _state

    init {
        viewModelScope.launch { requestDaily() }
        viewModelScope.launch {
            observeDaily().collect {
                when (it) {
                    is DailyPictureRepository.DailyResult.Loaded -> {
                        val localDateTime =
                            it.daily.date.toLocalDateTime(TimeZone.currentSystemDefault())
                        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

                        _state.value = _state.value.copy(
                            title = it.daily.title,
                            date = with(localDateTime) { "$dayOfMonth. $monthNumber. $year" },
                            isTodayVisible = localDateTime.date == today,
                            explanationState = ExplanationState.Explanation(
                                content = it.daily.explanation,
                            ),
                            imageState = when (it.daily.image) {
                                is Image.Loaded -> it.daily.image.image.let(ImageState::Image)
                                is Image.LoadingError -> ImageState.Frown
                                is Image.NotLoadedYet -> ImageState.Shimmer
                            },
                        )
                    }

                    is DailyPictureRepository.DailyResult.Loading -> {
                        _state.value = _state.value.copy(
                            imageState = ImageState.Shimmer,
                            explanationState = ExplanationState.Shimmer,
                        )
                    }

                    is DailyPictureRepository.DailyResult.Error -> TODO()
                }
            }
        }
    }

    data class State(
        val isDailyVisible: Boolean = false,

        val imageState: ImageState = ImageState.Shimmer,
        val title: String = "",
        val date: String = "",
        val today: String,
        val isTodayVisible: Boolean = false,

        val explanationTitle: String,
        val explanationState: ExplanationState = ExplanationState.Shimmer,
    ) {
        sealed class ImageState {
            object Shimmer : ImageState()
            object Frown : ImageState()
            data class Image(val image: Bitmap) : ImageState()
        }

        sealed class ExplanationState {
            object Shimmer : ExplanationState()
            data class Explanation(val content: String) : ExplanationState()
        }
    }
}
