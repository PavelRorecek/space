package com.pavelrorecek.feature.model

internal data class Launch(
    val id: Id,
    val name: String,
    val livestreamUrl: String?,
    val wikipediaUrl: String?,
) {

    @JvmInline
    value class Id(val value: String)
}
