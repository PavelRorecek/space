package com.pavelrorecek.feature.dailypicture.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pavelrorecek.core.design.BaseScreen
import com.pavelrorecek.feature.dailypicture.presentation.DailyPictureViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
public fun DailyPictureScreen() {
    val viewModel: DailyPictureViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    DailyPictureScreen(
        title = state.title,
        date = state.date,
        todayTitle = state.today,
        picture = state.picture,
        explanation = state.explanation,
        explanationTitle = state.explanationTitle,
    )
}

@Composable
internal fun DailyPictureScreen(
    title: String,
    date: String,
    todayTitle: String,
    picture: Bitmap?,
    explanation: String,
    explanationTitle: String,
) {
    if (picture != null) {
        BaseScreen {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        model = picture,
                        contentDescription = null,
                    )
                    Text(modifier = Modifier.padding(16.dp), text = title, color = Color.White)
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomStart),
                    ) {
                        Text(text = date, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        Text(text = todayTitle, color = Color.White, fontSize = 32.sp)
                    }
                }
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = explanationTitle,
                    fontSize = 32.sp,
                )
                Text(modifier = Modifier.padding(top = 8.dp), text = explanation)
            }
        }
    }
}
