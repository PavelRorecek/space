package com.pavelrorecek.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelrorecek.core.design.ui.Label
import com.pavelrorecek.core.design.ui.Title
import com.pavelrorecek.feature.domain.LaunchesNavigationController
import com.pavelrorecek.feature.domain.LaunchesRepository.LaunchesResult.Error
import com.pavelrorecek.feature.domain.LaunchesRepository.LaunchesResult.Loaded
import com.pavelrorecek.feature.domain.LaunchesRepository.LaunchesResult.Loading
import com.pavelrorecek.feature.domain.ObserveLaunchesUseCase
import com.pavelrorecek.feature.domain.RequestLaunchesUseCase
import com.pavelrorecek.feature.model.Launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class LaunchesViewModel(
    private val requestLaunches: RequestLaunchesUseCase,
    observeLaunches: ObserveLaunchesUseCase,
    strings: LaunchesStrings,
    private val navigation: LaunchesNavigationController,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private val livestream: Label = strings.livestream().let(::Label)
    private val wiki: Label = strings.wiki().let(::Label)

    init {
        viewModelScope.launch { requestLaunches() }
        viewModelScope.launch {
            observeLaunches().collect { result ->
                _state.value = when (result) {
                    is Loaded -> {
                        val launches = result.launches.map(::toState)
                        val pinned = launches.filter { it.isPinned }

                        State(
                            isPinnedVisible = pinned.isNotEmpty(),
                            pinned = Title(strings.pinned()),
                            unpinAll = Label(strings.unpinAll()),
                            pinnedLaunches = pinned,
                            upcoming = Title(strings.upcoming()),
                            sortBy = Label(strings.sortBy()),
                            upcomingLaunches = launches,
                        )
                    }

                    is Loading -> {
                        _state.value // TODO
                    }

                    is Error -> {
                        _state.value // TODO
                    }
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch { requestLaunches() }
    }

    private fun toState(model: Launch) = State.Launch(
        id = model.id,
        name = model.name.let(::Title),
        launchIn = "TODO".let(::Label), // TODO
        livestream = livestream,
        onLiveStream = { model.livestreamUrl?.let(navigation::openUrl) },
        wiki = wiki,
        onWiki = { model.wikipediaUrl?.let(navigation::openUrl) },
        isPinned = false,
    )

    data class State(
        val isPinnedVisible: Boolean = false,
        val pinned: Title = Title(""),
        val unpinAll: Label = Label(""),
        val pinnedLaunches: List<Launch> = emptyList(),

        val upcoming: Title = Title(""),
        val sortBy: Label = Label(""),
        val upcomingLaunches: List<Launch> = emptyList(),
    ) {

        data class Launch(
            val id: com.pavelrorecek.feature.model.Launch.Id,
            val name: Title,
            val launchIn: Label,
            val livestream: Label,
            val onLiveStream: () -> Unit,
            val wiki: Label,
            val onWiki: () -> Unit,
            val isPinned: Boolean,
        )
    }
}
