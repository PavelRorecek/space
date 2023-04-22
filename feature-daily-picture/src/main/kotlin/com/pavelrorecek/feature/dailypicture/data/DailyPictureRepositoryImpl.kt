package com.pavelrorecek.feature.dailypicture.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.pavelrorecek.feature.dailypicture.domain.DailyPictureRepository
import com.pavelrorecek.feature.dailypicture.domain.DailyPictureRepository.DailyResult
import com.pavelrorecek.feature.dailypicture.model.Daily
import com.pavelrorecek.feature.dailypicture.model.Daily.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDate
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

internal class DailyPictureRepositoryImpl(
    private val context: Context,
    private val api: DailyApi,
    private val dao: DailyDao,
) : DailyPictureRepository {

    private val _daily: MutableStateFlow<DailyResult?> = MutableStateFlow(null)

    @Suppress("unused")
    private suspend fun releaseApiCall(): DailyDto? {
        return runCatching { api.daily() }.getOrNull()
    }

    @Suppress("all")
    private suspend fun mockApiCall(): DailyDto? {
        delay(2000)
        return DailyDto(
            date = "2023-04-22",
            explanation = "In visible light NGC 1333 is seen as a reflection nebula, dominated " +
                "by bluish hues characteristic of starlight reflected by interstellar dust. " +
                "A mere 1,000 light-years distant toward the heroic constellation Perseus, " +
                "it lies at the edge of a large, star-forming molecular cloud. This Hubble " +
                "Space Telescope close-up frames a region just over 1 light-year wide at the " +
                "estimated distance of NGC 1333. It shows details of the dusty region along " +
                "with telltale hints of contrasty red emission from Herbig-Haro objects, " +
                "jets and shocked glowing gas emanating from recently formed stars. In " +
                "fact, NGC 1333 contains hundreds of stars less than a million years old, " +
                "most still hidden from optical telescopes by the pervasive stardust. The " +
                "chaotic environment may be similar to one in which our own Sun formed over " +
                "4.5 billion years ago. Hubble's stunning image of the stellar nursery was " +
                "released to celebrate the 33rd anniversary of the space telescope's launch. " +
                "Watch: Planet Earth's annual Lyrid Meteor Shower",
            hdurl = "https://apod.nasa.gov/apod/image/2304/NGC1333HST33rd.png",
            title = "NGC 1333: Stellar Nursery in Perseus",
            url = "https://apod.nasa.gov/apod/image/2304/NGC1333HST33rd_800.png",
        )
    }

    override suspend fun load() {
        _daily.value = DailyResult.Loading

        val dto = releaseApiCall()

        if (dto != null) {
            val title = dto.title
            val explanation = dto.explanation
            val lowResUrl = dto.url
            val highResUrl = dto.hdurl
            val date = dto.date.toLocalDate().atStartOfDayIn(TimeZone.of("US/Pacific"))

            val noImageDaily = Daily(
                title = title,
                explanation = explanation,
                date = date,
                image = Image.NotLoadedYet,
            )
            _daily.value = DailyResult.Loaded(noImageDaily)
            withContext(Dispatchers.IO) { dao.deleteAll() }
            storeDailyToPersistence(noImageDaily)

            val lowresImage = lowResUrl.loadBitmap()
            if (lowresImage != null) {
                val lowresDaily = Daily(
                    title = title,
                    explanation = explanation,
                    date = date,
                    image = lowresImage.let(Image::Loaded),
                )
                _daily.value = DailyResult.Loaded(lowresDaily)
                storeDailyToPersistence(lowresDaily)
            }

            val highresImage = highResUrl.loadBitmap()
            if (highresImage != null) {
                val highresDaily = Daily(
                    title = title,
                    explanation = explanation,
                    date = date,
                    image = highresImage.let(Image::Loaded),
                )
                _daily.value = DailyResult.Loaded(highresDaily)
                storeDailyToPersistence(highresDaily)
            }

            if (lowresImage == null && highresImage == null) {
                val imageErrorDaily = Daily(
                    title = title,
                    explanation = explanation,
                    date = date,
                    image = Image.LoadingError,
                )
                _daily.value = DailyResult.Loaded(imageErrorDaily)
                storeDailyToPersistence(imageErrorDaily)
            }
        } else {
            val daily = loadDailyFromPersistence()
            if (daily != null) {
                _daily.value = DailyResult.Loaded(daily)
            } else {
                _daily.value = DailyResult.Error
            }
        }
    }

    private suspend fun String.loadBitmap() = withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(context).data(this@loadBitmap).build()
        val response = Coil.imageLoader(context).execute(request)

        (response as? SuccessResult)?.drawable?.toBitmap()
    }

    private suspend fun storeDailyToPersistence(daily: Daily) {
        withContext(Dispatchers.IO) {
            dao.insert(
                DailyEo(
                    title = daily.title,
                    explanation = daily.explanation,
                    date = daily.date.toEpochMilliseconds(),
                    imagePath = when (daily.image) {
                        is Image.Loaded -> saveBitmapToInternalStorage(daily.image.image)
                        is Image.LoadingError -> null
                        is Image.NotLoadedYet -> null
                    },
                ),
            )
        }
    }

    private fun saveBitmapToInternalStorage(bitmap: Bitmap): String? {
        val file = File(context.filesDir, "daily.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()

        return file.path
    }

    private fun loadBitmapFromInternalStorage(path: String): Bitmap? {
        val file = File(path)
        val stream = FileInputStream(file)
        val bitmap = BitmapFactory.decodeStream(stream)
        stream.close()

        return bitmap
    }

    private suspend fun loadDailyFromPersistence() = withContext(Dispatchers.IO) {
        dao.first()?.let {
            Daily(
                title = it.title,
                explanation = it.explanation,
                date = Instant.fromEpochMilliseconds(it.date),
                image = it.imagePath
                    ?.let(::loadBitmapFromInternalStorage)
                    ?.let(Image::Loaded)
                    ?: Image.LoadingError,
            )
        }
    }

    override fun observe(): Flow<DailyResult> = _daily.filterNotNull()
}
