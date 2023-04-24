package com.pavelrorecek.feature.ui

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pavelrorecek.core.design.ui.BaseScreen
import com.pavelrorecek.core.design.ui.Label
import com.pavelrorecek.core.design.ui.Title
import com.pavelrorecek.feature.dailyPicture.R
import com.pavelrorecek.feature.model.Launch
import com.pavelrorecek.feature.presentation.LaunchesViewModel
import org.koin.androidx.compose.koinViewModel

private val ColorScheme.disabledWikiTint get() = Color(0xFFC5C5C5)
private val ColorScheme.pinnedBadgeBackground get() = Color(0xFFF1C543)
private val ColorScheme.pinnedBadgePinColor get() = Color.White
private val ColorScheme.unpinnedBadgeBackground get() = Color(0xFFF3F3F3)
private val ColorScheme.unpinnedBadgePinColor get() = Color(0xFF939393)
private val ColorScheme.disabledLivestreamBackground get() = Color(0xFF8B8B8B)
private val ColorScheme.enabledLivestreamBackground get() = Color(0xFFEE807D)

@Composable
public fun LaunchesScreen() {
    val viewModel: LaunchesViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    DailyScreen(
        state = state,
        onRefresh = viewModel::onRefresh,
        onUnpinAll = viewModel::onUnpinAll,
        onLiveStream = viewModel::onLiveStream,
        onWiki = viewModel::onWiki,
        onPin = viewModel::onPin,
    )
}

@Composable
internal fun DailyScreen(
    state: LaunchesViewModel.State,
    onRefresh: () -> Unit,
    onUnpinAll: () -> Unit,
    onLiveStream: (Launch.Id) -> Unit,
    onWiki: (Launch.Id) -> Unit,
    onPin: (Launch.Id) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val launchWidth = remember(configuration) { configuration.screenWidthDp.dp - 64.dp }

    BaseScreen {
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = onRefresh,
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                if (state.isPinnedVisible) {
                    PinnedLaunches(
                        modifier = Modifier
                            .padding(horizontal = 32.dp),
                        launchWidth = launchWidth,
                        pinned = state.pinned,
                        unpinAll = state.unpinAll,
                        onUnpinAll = onUnpinAll,
                        pinnedLaunches = state.pinnedLaunches,
                        onLiveStream = onLiveStream,
                        onWiki = onWiki,
                        onPin = onPin,
                    )
                    Spacer(modifier = Modifier.size(64.dp))
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    text = state.upcoming.value,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.size(16.dp))
                if (state.isUpcomingLaunchesVisible) {
                    val lazyListState = rememberLazyListState()
                    LazyRow(
                        state = lazyListState,
                        flingBehavior = rememberSnapFlingBehavior(lazyListState),
                        contentPadding = PaddingValues(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        state.upcomingLaunches.chunked(2).forEach { column ->
                            item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(24.dp),
                                ) {
                                    column.forEach {
                                        LaunchItem(
                                            modifier = Modifier.width(launchWidth),
                                            launch = it,
                                            onLiveStream = onLiveStream,
                                            onWiki = onWiki,
                                            onPin = onPin,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                if (state.isUpcomingLaunchesLoadingVisible) {
                    LoadingLaunches(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        launchWidth = launchWidth,
                    )
                }
            }
        }
    }
}

@Composable
private fun PinnedLaunches(
    modifier: Modifier,
    launchWidth: Dp,
    pinned: Title,
    unpinAll: Label,
    pinnedLaunches: List<LaunchesViewModel.State.Launch>,
    onUnpinAll: () -> Unit,
    onLiveStream: (Launch.Id) -> Unit,
    onWiki: (Launch.Id) -> Unit,
    onPin: (Launch.Id) -> Unit,
) {
    Column(modifier) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = pinned.value,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                modifier = Modifier.clickable(onClick = onUnpinAll),
                text = unpinAll.value,
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            modifier = Modifier.animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            pinnedLaunches.forEach { launch ->
                LaunchItem(
                    modifier = Modifier.width(launchWidth),
                    launch = launch,
                    onLiveStream = onLiveStream,
                    onWiki = onWiki,
                    onPin = onPin,
                )
            }
        }
    }
}

@Composable
private fun LaunchItem(
    modifier: Modifier,
    launch: LaunchesViewModel.State.Launch,
    onLiveStream: (Launch.Id) -> Unit,
    onWiki: (Launch.Id) -> Unit,
    onPin: (Launch.Id) -> Unit,
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = launch.launchIn.value,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LivestreamButton(
                    label = launch.livestream.value,
                    onClick = { onLiveStream(launch.id) },
                    enabled = launch.isLivestreamVisible,
                )
                WikiButton(
                    label = launch.wiki.value,
                    onClick = { onWiki(launch.id) },
                    enabled = launch.isWikiVisible,
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        if (launch.isPinned) {
            PinnedBadge(onClick = { onPin(launch.id) })
        } else {
            UnpinnedBadge(onClick = { onPin(launch.id) })
        }
    }
}

@Composable
private fun LoadingLaunches(modifier: Modifier, launchWidth: Dp) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        repeat(2) {
            LoadingLaunch(
                modifier = Modifier.width(launchWidth),
            )
        }
    }
}

@Composable
private fun LoadingLaunch(modifier: Modifier) {
    Row(modifier.height(86.dp)) {
        Column(Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                        ),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(48.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                            ),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .height(12.dp)
                            .width(102.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                            ),
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(86.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                            ),
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(64.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                ),
        )
    }
}

@Composable
private fun LivestreamButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val background = if (enabled) {
        MaterialTheme.colorScheme.enabledLivestreamBackground
    } else {
        MaterialTheme.colorScheme.disabledLivestreamBackground
    }

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
    val disabledTint = MaterialTheme.colorScheme.disabledWikiTint

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
        backgroundColor = MaterialTheme.colorScheme.pinnedBadgeBackground,
        pinColor = MaterialTheme.colorScheme.pinnedBadgePinColor,
        onClick = onClick,
    )
}

@Composable
private fun UnpinnedBadge(onClick: () -> Unit) {
    Badge(
        backgroundColor = MaterialTheme.colorScheme.unpinnedBadgeBackground,
        pinColor = MaterialTheme.colorScheme.unpinnedBadgePinColor,
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
