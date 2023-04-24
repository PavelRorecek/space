package com.pavelrorecek.feature.presentation

internal interface LaunchesStrings {
    fun launchIn(days: Long, hours: Long, minutes: Long, seconds: Long): String
    fun livestream(): String
    fun pinned(): String
    fun sortBy(): String
    fun unpinAll(): String
    fun upcoming(): String
    fun wiki(): String
}
