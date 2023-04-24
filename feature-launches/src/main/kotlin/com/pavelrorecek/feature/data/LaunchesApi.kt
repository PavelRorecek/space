package com.pavelrorecek.feature.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

internal interface LaunchesApi {

    @GET("v4/launches")
    suspend fun launches(): List<LauchDto>
}

internal data class LauchDto(
    val id: String,
    val name: String,
    val links: LinksDto?,
    val upcoming: Boolean,
    @SerializedName("date_unix") val dateUnix: Long,
) {

    data class LinksDto(
        val webcast: String?,
        val wikipedia: String?,
    )
}
