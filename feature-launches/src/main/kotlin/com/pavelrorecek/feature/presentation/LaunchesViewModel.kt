package com.pavelrorecek.feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelrorecek.core.design.ui.Label
import com.pavelrorecek.core.design.ui.Title
import com.pavelrorecek.feature.domain.DeleteAllPinnedLaunchesUseCase
import com.pavelrorecek.feature.domain.DeletePinnedLaunchUseCase
import com.pavelrorecek.feature.domain.LaunchesNavigationController
import com.pavelrorecek.feature.domain.LaunchesRepository.LaunchesResult.Error
import com.pavelrorecek.feature.domain.LaunchesRepository.LaunchesResult.Loaded
import com.pavelrorecek.feature.domain.LaunchesRepository.LaunchesResult.Loading
import com.pavelrorecek.feature.domain.ObserveLaunchesUseCase
import com.pavelrorecek.feature.domain.ObservePinnedLaunchesUseCase
import com.pavelrorecek.feature.domain.RequestLaunchesUseCase
import com.pavelrorecek.feature.domain.StorePinnedLaunchUseCase
import com.pavelrorecek.feature.model.Launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class LaunchesViewModel(
    private val requestLaunches: RequestLaunchesUseCase,
    observeLaunches: ObserveLaunchesUseCase,
    observePinnedLaunches: ObservePinnedLaunchesUseCase,
    private val storePinnedLaunch: StorePinnedLaunchUseCase,
    private val deletePinnedLaunch: DeletePinnedLaunchUseCase,
    private val deleteAllPinnedLaunches: DeleteAllPinnedLaunchesUseCase,
    private val strings: LaunchesStrings,
    private val navigation: LaunchesNavigationController,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    private val livestream: Label = strings.livestream().let(::Label)
    private val wiki: Label = strings.wiki().let(::Label)

    init {
        viewModelScope.launch { requestLaunches() }
        viewModelScope.launch {
            observePinnedLaunches().collect { pinned ->
                _state.value = _state.value.copy(
                    isPinnedVisible = pinned.isNotEmpty(),
                    pinned = Title(strings.pinned()),
                    unpinAll = Label(strings.unpinAll()),
                    pinnedLaunches = pinned.map(::toState),
                )
            }
        }
        viewModelScope.launch {
            observeLaunches().collect { result ->
                _state.value = when (result) {
                    is Loaded -> {
                        val launches = result.launches.map(::toState)

                        _state.value.copy(
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

    fun onUnpinAll() {
        viewModelScope.launch { deleteAllPinnedLaunches() }
    }

    private fun toState(model: Launch) = State.Launch(
        id = model.id,
        name = model.name.let(::Title),
        launchIn = formatLaunchIn(model.launch).let(::Label),
        livestream = livestream,
        isLivestreamVisible = model.livestreamUrl != null,
        onLiveStream = { model.livestreamUrl?.let(navigation::openUrl) },
        wiki = wiki,
        isWikiVisible = model.wikipediaUrl != null,
        onWiki = { model.wikipediaUrl?.let(navigation::openUrl) },
        isPinned = model.isPinned,
        onPin = {
            viewModelScope.launch {
                if (model.isPinned) deletePinnedLaunch(model.id) else storePinnedLaunch(model)
            }
        },
    )

    private fun formatLaunchIn(endTime: Instant): String {
        val duration = endTime - Clock.System.now()

        return if (duration.isPositive()) {
            val days = duration.inWholeDays
            val hours = duration.inWholeHours % 24
            val minutes = duration.inWholeMinutes % 60
            val seconds = duration.inWholeSeconds % 60

            strings.launchIn(days, hours, minutes, seconds)
        } else {
            with(endTime.toLocalDateTime(TimeZone.currentSystemDefault())) {
                strings.launchedOn("$dayOfMonth. $monthNumber. $year")
            }
        }
    }

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
            val isLivestreamVisible: Boolean,
            val onLiveStream: () -> Unit,
            val wiki: Label,
            val isWikiVisible: Boolean,
            val onWiki: () -> Unit,
            val isPinned: Boolean,
            val onPin: () -> Unit,
        )
    }
}
