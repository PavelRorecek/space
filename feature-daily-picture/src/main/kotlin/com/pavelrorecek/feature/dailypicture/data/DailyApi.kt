package com.pavelrorecek.feature.dailypicture.data

import retrofit2.http.GET

internal interface DailyApi {

    @GET("planetary/apod")
    suspend fun daily(): DailyDto
}
