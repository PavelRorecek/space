package com.pavelrorecek.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pavelrorecek.core.design.AppTheme
import com.pavelrorecek.feature.dailypicture.ui.DailyScreen

public class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                DailyScreen()
            }
        }
    }
}
