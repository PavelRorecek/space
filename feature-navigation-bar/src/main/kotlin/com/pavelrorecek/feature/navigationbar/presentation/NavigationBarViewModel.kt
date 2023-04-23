package com.pavelrorecek.feature.navigationbar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelrorecek.core.navigation.domain.ObserveCurrentScreenUseCase
import com.pavelrorecek.core.navigation.model.Screen.DAILY
import com.pavelrorecek.core.navigation.model.Screen.LAUNCHES
import com.pavelrorecek.feature.navigationbar.domain.NavigationBarNavigationController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class NavigationBarViewModel(
    observeCurrentScreen: ObserveCurrentScreenUseCase,
    private val navigation: NavigationBarNavigationController,
    strings: NavigationBarStrings,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State(
            dailyTitle = strings.daily(),
            launchesTitle = strings.launches(),
        ),
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeCurrentScreen().collect {
                _state.value = _state.value.copy(
                    isDailySelected = it == DAILY,
                    isLaunchesSelected = it == LAUNCHES,
                )
            }
        }
    }

    fun onDaily() {
        navigation.goToDaily()
    }

    fun onLaunches() {
        navigation.goToLaunches()
    }

    data class State(
        val dailyTitle: String,
        val isDailySelected: Boolean = false,
        val launchesTitle: String,
        val isLaunchesSelected: Boolean = false,
    )
}
