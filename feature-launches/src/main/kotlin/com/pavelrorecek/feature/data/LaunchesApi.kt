package com.pavelrorecek.feature.data

import retrofit2.http.GET

internal interface LaunchesApi {

    @GET("v4/launches")
    suspend fun launches(): List<LauchDto>
}

internal data class LauchDto(
    val id: String,
    val name: String,
    val links: LinksDto?,
) {

    data class LinksDto(
        val webcast: String?,
        val wikipedia: String?,
    )
}
