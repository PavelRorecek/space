package com.pavelrorecek.feature.dailypicture.model

import android.graphics.Bitmap
import kotlinx.datetime.Instant

internal data class Daily(
    val title: String,
    val explanation: String,
    val date: Instant,
    val image: Image,
) {

    sealed class Image {
        object NotLoadedYet : Image()
        object LoadingError : Image()
        data class Loaded(val image: Bitmap) : Image()
    }
}
