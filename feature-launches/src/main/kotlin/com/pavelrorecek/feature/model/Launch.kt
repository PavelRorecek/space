package com.pavelrorecek.feature.model

import kotlinx.datetime.Instant

internal data class Launch(
    val id: Id,
    val name: String,
    val livestreamUrl: String?,
    val wikipediaUrl: String?,
    val launch: Instant,
) {

    @JvmInline
    value class Id(val value: String)
}
