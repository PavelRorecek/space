package com.pavelrorecek.feature.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pinnedLaunch")
internal data class PinnedLaunchEo(
    @PrimaryKey val id: String,
    val name: String,
    val livestreamUrl: String?,
    val wikipediaUrl: String?,
    val launch: Long,
)
