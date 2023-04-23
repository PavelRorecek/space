package com.pavelrorecek.feature.presentation

import androidx.lifecycle.ViewModel
import com.pavelrorecek.core.design.ui.Label
import com.pavelrorecek.core.design.ui.Title
import com.pavelrorecek.feature.domain.LaunchesNavigationController
import com.pavelrorecek.feature.presentation.LaunchesViewModel.State.Launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class LaunchesViewModel(
    strings: LaunchesStrings,
    private val navigation: LaunchesNavigationController,
) : ViewModel() {

    private val _state: MutableStateFlow<State>
    val state: StateFlow<State>

    init {
        val launches = listOf(
            mockLaunch(
                id = Launch.Id("0"),
                name = Title("Starlink 4-42 (v1.2)"),
                launchIn = Label("Launches in 20d 6h 20m 23s"),
                isPinned = true,
            ),
            mockLaunch(
                id = Launch.Id("1"),
                name = Title("Starlink 4-12 (v1.2)"),
                launchIn = Label("Launches in 14d 6h 20m 23s"),
                isPinned = true,
            ),
            mockLaunch(
                id = Launch.Id("2"),
                name = Title("Starlink 4-54 (v1.2)"),
                launchIn = Label("Launches in 10d 6h 20m 23s"),
                isPinned = false,
            ),
            mockLaunch(
                id = Launch.Id("3"),
                name = Title("Starlink 4-123 (v1.2)"),
                launchIn = Label("Launches in 10d 6h 20m 23s"),
                isPinned = false,
            ),
            mockLaunch(
                id = Launch.Id("4"),
                name = Title("Starlink 4-67 (v1.2)"),
                launchIn = Label("Launches in 10d 6h 20m 23s"),
                isPinned = false,
            ),
        )

        _state = MutableStateFlow(
            State(
                isPinnedVisible = true,
                pinned = Title(strings.pinned()),
                unpinAll = Label(strings.unpinAll()),
                pinnedLaunches = launches.filter { it.isPinned },
                upcoming = Title(strings.upcoming()),
                sortBy = Label(strings.sortBy()),
                upcomingLaunches = launches,
            ),
        )
        state = _state
    }

    private fun mockLaunch(
        id: Launch.Id = Launch.Id("4"),
        name: Title = Title("Starlink 4-67 (v1.2)"),
        launchIn: Label = Label("Launches in 10d 6h 20m 23s"),
        isPinned: Boolean = true,
    ) = Launch(
        id = id,
        name = name,
        launchIn = launchIn,
        livestream = Label("Livestream"),
        onLiveStream = { navigation.openUrl("https://www.youtube.com/watch?v=0a_00nJ_Y88") },
        wiki = Label("Wiki"),
        onWiki = { navigation.openUrl("https://en.wikipedia.org/wiki/DemoSat") },
        isPinned = isPinned,
    )

    @Suppress("EmptyFunctionBlock")
    fun onRefresh() {}

    data class State(
        val isPinnedVisible: Boolean,
        val pinned: Title,
        val unpinAll: Label,
        val pinnedLaunches: List<Launch>,

        val upcoming: Title,
        val sortBy: Label,
        val upcomingLaunches: List<Launch>,
    ) {

        data class Launch(
            val id: Id,
            val name: Title,
            val launchIn: Label,
            val livestream: Label,
            val onLiveStream: () -> Unit,
            val wiki: Label,
            val onWiki: () -> Unit,
            val isPinned: Boolean,
        ) {

            @JvmInline
            value class Id(val value: String)
        }
    }
}
