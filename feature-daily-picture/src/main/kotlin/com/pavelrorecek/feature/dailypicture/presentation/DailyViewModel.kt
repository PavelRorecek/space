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
            errorMessage = strings.loadingError(),
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
                            isErrorVisible = false,
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
                            isExplanationVisible = true,
                        )
                    }

                    is DailyPictureRepository.DailyResult.Loading -> {
                        _state.value = _state.value.copy(
                            isErrorVisible = false,
                            title = "",
                            date = "",
                            isTodayVisible = false,
                            imageState = ImageState.Shimmer,
                            explanationState = ExplanationState.Shimmer,
                            isExplanationVisible = true,
                        )
                    }

                    is DailyPictureRepository.DailyResult.Error -> {
                        _state.value = _state.value.copy(
                            isErrorVisible = true,
                            title = "",
                            date = "",
                            isTodayVisible = false,
                            imageState = ImageState.Frown,
                            isExplanationVisible = false,
                        )
                    }
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch { requestDaily() }
    }

    data class State(
        val isErrorVisible: Boolean = false,
        val errorMessage: String,

        val imageState: ImageState = ImageState.Shimmer,
        val title: String = "",
        val date: String = "",
        val today: String,
        val isTodayVisible: Boolean = false,

        val explanationTitle: String,
        val explanationState: ExplanationState = ExplanationState.Shimmer,
        val isExplanationVisible: Boolean = true,
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
