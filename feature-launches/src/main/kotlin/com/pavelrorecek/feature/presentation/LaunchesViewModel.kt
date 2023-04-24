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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class LaunchesViewModel(
    private val requestLaunches: RequestLaunchesUseCase,
    private val observeLaunches: ObserveLaunchesUseCase,
    observePinnedLaunches: ObservePinnedLaunchesUseCase,
    private val storePinnedLaunch: StorePinnedLaunchUseCase,
    private val deletePinnedLaunch: DeletePinnedLaunchUseCase,
    private val deleteAllPinnedLaunches: DeleteAllPinnedLaunchesUseCase,
    private val strings: LaunchesStrings,
    private val navigation: LaunchesNavigationController,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State(
            upcoming = Title(strings.upcoming()),
            pinned = Title(strings.pinned()),
            unpinAll = Label(strings.unpinAll()),
        ),
    )
    val state: StateFlow<State> = _state

    private val livestream: Label = strings.livestream().let(::Label)
    private val wiki: Label = strings.wiki().let(::Label)

    init {
        viewModelScope.launch { requestLaunches() }
        viewModelScope.launch {
            observePinnedLaunches().collect { pinned ->
                _state.value = _state.value.copy(
                    isPinnedVisible = pinned.isNotEmpty(),
                    pinnedLaunches = pinned.map(::toState),
                )
            }
        }
        viewModelScope.launch {
            observeLaunches().collect { result ->
                _state.value = when (result) {
                    is Loaded -> {
                        _state.value.copy(
                            upcomingLaunches = result.launches.map(::toState),
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

    fun onLiveStream(id: Launch.Id) {
        viewModelScope.launch {
            (observeLaunches().first() as Loaded).launches.firstOrNull { it.id == id }
                ?.livestreamUrl
                ?.let(navigation::openUrl)
        }
    }

    fun onWiki(id: Launch.Id) {
        viewModelScope.launch {
            (observeLaunches().first() as Loaded).launches.firstOrNull { it.id == id }
                ?.wikipediaUrl
                ?.let(navigation::openUrl)
        }
    }

    fun onPin(id: Launch.Id) {
        viewModelScope.launch {
            val launch = (observeLaunches().first() as Loaded).launches.firstOrNull { it.id == id }

            when (launch?.isPinned) {
                true -> deletePinnedLaunch(id)
                false -> storePinnedLaunch(launch)
                null -> { /* pass */
                }
            }
        }
    }

    private fun toState(model: Launch) = State.Launch(
        id = model.id,
        name = model.name.let(::Title),
        launchIn = formatLaunchIn(model.launch).let(::Label),
        livestream = livestream,
        isLivestreamVisible = model.livestreamUrl != null,
        wiki = wiki,
        isWikiVisible = model.wikipediaUrl != null,
        isPinned = model.isPinned,
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
        val pinned: Title,
        val unpinAll: Label,
        val pinnedLaunches: List<Launch> = emptyList(),

        val upcoming: Title,
        val upcomingLaunches: List<Launch> = emptyList(),
    ) {

        data class Launch(
            val id: com.pavelrorecek.feature.model.Launch.Id,
            val name: Title,
            val launchIn: Label,
            val livestream: Label,
            val isLivestreamVisible: Boolean,
            val wiki: Label,
            val isWikiVisible: Boolean,
            val isPinned: Boolean,
        )
    }
}
