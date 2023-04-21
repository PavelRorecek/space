package com.pavelrorecek.feature.dailypicture.data

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.pavelrorecek.feature.dailypicture.domain.DailyPictureRepository
import com.pavelrorecek.feature.dailypicture.model.Daily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext

internal class DailyPictureRepositoryImpl(
    private val context: Context,
) : DailyPictureRepository {

    private val _daily = MutableStateFlow(null as Daily?)

    override suspend fun load() {
        val title = "Solar Eclipse from Western Australia"
        val explanation = "Along a narrow path that mostly avoided landfall, the shadow of the " +
            "New Moon raced across planet Earth's southern hemisphere on April 20 to create" +
            " a rare annular-total or hybrid solar eclipse. A mere 62 seconds of totality" +
            " could be seen though, when the dark central lunar shadow just grazed the" +
            " North West Cape, a peninsula in western Australia. From top to bottom these" +
            " panels capture the beginning, middle, and end of that fleeting total eclipse" +
            " phase. At start and finish, solar prominences and beads of sunlight stream past" +
            " the lunar limb. At mid-eclipse the central frame reveals the sight only easily" +
            " visible during totality and most treasured by eclipse chasers, the magnificent" +
            " corona of the active Sun. Of course eclipses tend to come in pairs. On May 5," +
            " the next Full Moon will just miss the dark inner part of Earth's shadow in a" +
            " penumbral lunar eclipse."
        val lowResUrl = "https://apod.nasa.gov/apod/image/2304/PSX_20230420_140324h1024.jpg"
        val highResUrl = "https://apod.nasa.gov/apod/image/2304/PSX_20230420_140324.jpg"

        _daily.value = Daily(
            title = title,
            explanation = explanation,
            picture = lowResUrl.loadBitmap()!!, // TODO
        )

        _daily.value = Daily(
            title = title,
            explanation = explanation,
            picture = highResUrl.loadBitmap()!!, // TODO
        )
    }

    private suspend fun String.loadBitmap() = withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(context).data(this@loadBitmap).build()
        val response = Coil.imageLoader(context).execute(request)

        (response as? SuccessResult)?.drawable?.toBitmap()
    }

    override fun observe(): Flow<Daily> = _daily.filterNotNull()
}
