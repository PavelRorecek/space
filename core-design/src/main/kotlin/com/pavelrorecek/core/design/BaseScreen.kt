package com.pavelrorecek.core.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
public fun BaseScreen(
    content: @Composable () -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        content()
    }
}
