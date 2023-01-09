package com.pavelrorecek.feature.dailypicture.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class DailyPictureViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    data class State(
        val text: String = "Daily",
    )
}
