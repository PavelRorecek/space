package com.pavelrorecek.feature.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pavelrorecek.core.design.ui.BaseScreen
import com.pavelrorecek.feature.presentation.LaunchesViewModel
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import org.koin.androidx.compose.koinViewModel

@Composable
public fun LaunchesScreen() {
    val viewModel: LaunchesViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    DailyScreen(
        state = state,
        onRefresh = viewModel::onRefresh,
    )
}

@Composable
internal fun DailyScreen(
    state: LaunchesViewModel.State,
    onRefresh: () -> Unit,
) {
    BaseScreen {
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = onRefresh,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
            ) {
                Row {
                    Text(modifier = Modifier.weight(1f), text = state.pinned.value)
                    Text(text = state.unpinAll.value)
                }
                state.pinnedLaunches.forEach {
                    LaunchItem(it)
                }
                Row {
                    Text(modifier = Modifier.weight(1f), text = state.upcoming.value)
                    Text(text = state.sortBy.value)
                }
                val lazyListState = rememberLazyListState()

                LazyRow(
                    state = lazyListState,
                    flingBehavior = rememberSnapperFlingBehavior(lazyListState),
                ) {
                    state.upcomingLaunches.chunked(2).forEach { column ->
                        item {
                            Column {
                                column.forEach { LaunchItem(launch = it) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LaunchItem(launch: LaunchesViewModel.State.Launch) {
    Row(
        modifier = Modifier
            .background(Color.Gray)
            .padding(16.dp),
    ) {
        Column {
            Text(text = launch.name.value)
            Text(text = launch.launchIn.value)
            Row {
                Button(onClick = launch.onLiveStream) {
                    Text(text = launch.livestream.value)
                }
                Button(onClick = launch.onWiki) {
                    Text(text = launch.wiki.value)
                }
            }
        }
        Text(text = launch.isPinned.toString())
    }
}
