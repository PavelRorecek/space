package com.pavelrorecek.feature.dailypicture.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pavelrorecek.core.design.ui.BaseScreen
import com.pavelrorecek.feature.dailyPicture.R
import com.pavelrorecek.feature.dailypicture.presentation.DailyViewModel
import com.pavelrorecek.feature.dailypicture.presentation.DailyViewModel.State.ExplanationState
import org.koin.androidx.compose.koinViewModel

private val shimmerFrownHeight = 500.dp
private val frownTint = Color.White.copy(alpha = 0.2f)

@Composable
public fun DailyScreen() {
    val viewModel: DailyViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    DailyScreen(
        state = state,
        onRefresh = viewModel::onRefresh,
    )
}

@Composable
internal fun DailyImage(
    state: DailyViewModel.State,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .animateContentSize(),
    ) {
        Crossfade(targetState = state, label = "") { state ->
            when (state.imageState) {
                is DailyViewModel.State.ImageState.Shimmer -> {
                    Box(
                        modifier = Modifier
                            .height(shimmerFrownHeight)
                            .fillMaxWidth()
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                            ),
                    )
                }

                is DailyViewModel.State.ImageState.Frown -> {
                    Box(
                        modifier = Modifier
                            .height(shimmerFrownHeight)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onBackground),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.frown),
                            colorFilter = ColorFilter.tint(frownTint),
                            contentDescription = null,
                        )
                    }
                }

                is DailyViewModel.State.ImageState.Image -> {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        model = state.imageState.image,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
internal fun DailyScreen(
    state: DailyViewModel.State,
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
                Box(modifier = Modifier.fillMaxWidth()) {
                    DailyImage(state = state)
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = state.title,
                        color = Color.White,
                    )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomStart),
                    ) {
                        Text(
                            text = state.date,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                        )
                        if (state.isTodayVisible) {
                            Text(text = state.today, color = Color.White, fontSize = 32.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.size(24.dp))
                if (state.isExplanationVisible) {
                    Explanation(
                        title = state.explanationTitle,
                        explanationState = state.explanationState,
                    )
                }
                if (state.isErrorVisible) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = state.errorMessage,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun Explanation(title: String, explanationState: ExplanationState) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Crossfade(targetState = explanationState, label = "Explanation") { state ->
            when (state) {
                is ExplanationState.Explanation -> {
                    Text(
                        text = state.content,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                is ExplanationState.Shimmer -> {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(6) {
                            Box(
                                modifier = Modifier
                                    .height(20.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                    )
                                    .background(MaterialTheme.colorScheme.onBackground),
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth(0.8f)
                                .clip(RoundedCornerShape(6.dp))
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                )
                                .background(MaterialTheme.colorScheme.onBackground),
                        )
                    }
                }
            }
        }
    }
}
