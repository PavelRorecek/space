package com.pavelrorecek.feature.dailypicture.model

import android.graphics.Bitmap

internal data class Daily(
    val title: String,
    val explanation: String,
    val picture: Bitmap,
)
