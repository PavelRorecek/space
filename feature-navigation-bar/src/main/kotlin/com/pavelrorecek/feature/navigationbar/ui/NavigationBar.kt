package com.pavelrorecek.feature.navigationbar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pavelrorecek.feature.navigationBar.R
import com.pavelrorecek.feature.navigationbar.presentation.NavigationBarViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
public fun NavigationBar(modifier: Modifier) {
    val viewModel: NavigationBarViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    // TODO bg color
    BottomAppBar(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            BarItem(
                painter = painterResource(id = R.drawable.ic_daily),
                title = state.dailyTitle,
                isSelected = state.isDailySelected,
                onClick = viewModel::onDaily,
            )
            BarItem(
                painter = painterResource(id = R.drawable.ic_launches),
                title = state.launchesTitle,
                isSelected = state.isLaunchesSelected,
                onClick = viewModel::onLaunches,
            )
        }
    }
}

@Composable
private fun BarItem(
    painter: Painter,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .defaultMinSize(minWidth = 120.dp)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val background = if (isSelected) {
            MaterialTheme.colorScheme.onBackground
        } else {
            Color.Transparent
        }
        val iconTint = if (isSelected) {
            MaterialTheme.colorScheme.background
        } else {
            MaterialTheme.colorScheme.onBackground
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(background)
                .padding(horizontal = 16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                painter = painter,
                contentDescription = null,
                tint = iconTint,
            )
        }
        Text(text = title)
    }
}
