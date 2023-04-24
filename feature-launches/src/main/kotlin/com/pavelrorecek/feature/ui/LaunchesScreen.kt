package com.pavelrorecek.feature.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pavelrorecek.core.design.ui.BaseScreen
import com.pavelrorecek.feature.dailyPicture.R
import com.pavelrorecek.feature.presentation.LaunchesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
public fun LaunchesScreen() {
    val viewModel: LaunchesViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    DailyScreen(
        state = state,
        onRefresh = viewModel::onRefresh,
        onUnpinAll = viewModel::onUnpinAll,
    )
}

@Composable
internal fun DailyScreen(
    state: LaunchesViewModel.State,
    onRefresh: () -> Unit,
    onUnpinAll: () -> Unit,
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
                    .verticalScroll(rememberScrollState()),
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(top = 32.dp),
                ) {
                    if (state.isPinnedVisible) {
                        Row {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = state.pinned.value,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                modifier = Modifier.clickable(onClick = onUnpinAll),
                                text = state.unpinAll.value,
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        state.pinnedLaunches.forEach { launch ->
                            LaunchItem(
                                modifier = Modifier.fillMaxWidth(),
                                launch = launch,
                            )
                        }
                        Spacer(modifier = Modifier.size(64.dp))
                    }
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    text = state.upcoming.value,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.size(16.dp))

                val lazyListState = rememberLazyListState()
                LazyRow(
                    state = lazyListState,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState),
                    contentPadding = PaddingValues(horizontal = 32.dp),
                ) {
                    state.upcomingLaunches.chunked(2).forEach { column ->
                        item {
                            Column {
                                column.forEach {
                                    LaunchItem(
                                        modifier = Modifier.width(300.dp),
                                        launch = it,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LaunchItem(
    modifier: Modifier,
    launch: LaunchesViewModel.State.Launch,
) {
    Row(modifier.height(86.dp)) {
        Column(Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .background(Color.Black),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column {
                    Text(
                        text = launch.name.value,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = launch.launchIn.value,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LivestreamButton(
                    label = launch.livestream.value,
                    onClick = launch.onLiveStream,
                    enabled = launch.isLivestreamVisible,
                )
                WikiButton(
                    label = launch.wiki.value,
                    onClick = launch.onWiki,
                    enabled = launch.isWikiVisible,
                )
            }
        }
        if (launch.isPinned) {
            PinnedBadge(onClick = launch.onPin)
        } else {
            UnpinnedBadge(onClick = launch.onPin)
        }
    }
}

@Composable
private fun LivestreamButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val background = if (enabled) Color(0xFFEE807D) else Color(0xFF8B8B8B)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .run { if (enabled) clickable(onClick = onClick) else this }
            .background(background)
            .padding(vertical = 2.dp)
            .padding(start = 2.dp, end = 8.dp),
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_livestream),
                contentDescription = null,
                tint = Color.White,
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = label,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun WikiButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val disabledTint = Color(0xFF8B8B8B)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .run { if (enabled) clickable(onClick = onClick) else this }
            .padding(vertical = 2.dp)
            .padding(start = 2.dp, end = 8.dp),
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_wiki),
                contentDescription = null,
                tint = if (enabled) LocalContentColor.current else disabledTint,
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = label,
                color = if (enabled) Color.Unspecified else disabledTint,
            )
        }
    }
}

@Composable
private fun PinnedBadge(onClick: () -> Unit) {
    Badge(
        backgroundColor = Color(0xFFF1C543),
        pinColor = Color.White,
        onClick = onClick,
    )
}

@Composable
private fun UnpinnedBadge(onClick: () -> Unit) {
    Badge(
        backgroundColor = Color(0xFFF3F3F3),
        pinColor = Color(0xFF939393),
        onClick = onClick,
    )
}

@Composable
private fun Badge(
    backgroundColor: Color,
    pinColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .size(64.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(id = R.drawable.ic_pin),
            contentDescription = null,
            tint = pinColor,
        )
    }
}
