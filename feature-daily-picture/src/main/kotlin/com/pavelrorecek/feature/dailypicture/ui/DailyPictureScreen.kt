package com.pavelrorecek.feature.dailypicture.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import com.pavelrorecek.core.design.BaseScreen
import com.pavelrorecek.feature.dailypicture.presentation.DailyPictureViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
public fun DailyPictureScreen() {
    val viewModel: DailyPictureViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    DailyPictureScreen(
        text = state.text,
    )
}

@Composable
internal fun DailyPictureScreen(
    text: String,
) {
    BaseScreen {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
            Text(text = text)
        }
    }
}
