package com.pavelrorecek.feature.dailypicture.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelrorecek.feature.dailypicture.domain.LoadDailyUseCase
import com.pavelrorecek.feature.dailypicture.domain.ObserveDailyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DailyPictureViewModel(
    private val loadDailyPicture: LoadDailyUseCase,
    private val observeDailyPicture: ObserveDailyUseCase,
    strings: DailyStrings,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State(
            date = "21. 4. 2022",
            today = strings.today(),
            explanationTitle = strings.explanation(),
        ),
    )
    val state: StateFlow<State> = _state

    init {
        viewModelScope.launch { loadDailyPicture() }
        viewModelScope.launch {
            observeDailyPicture().collect {
                _state.value = _state.value.copy(
                    title = it.title,
                    explanation = it.explanation,
                    picture = it.picture,
                )
            }
        }
    }

    data class State(
        val picture: Bitmap? = null,
        val title: String = "",
        val date: String,
        val today: String,
        val explanation: String = "",
        val explanationTitle: String,
    )
}
