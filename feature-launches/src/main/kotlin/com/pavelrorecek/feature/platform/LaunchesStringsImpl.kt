package com.pavelrorecek.feature.platform

import android.content.Context
import com.pavelrorecek.feature.dailyPicture.R
import com.pavelrorecek.feature.presentation.LaunchesStrings

internal class LaunchesStringsImpl(
    private val context: Context,
) : LaunchesStrings {

    override fun launchIn(days: Long, hours: Long, minutes: Long, seconds: Long) =
        context.getString(R.string.launches_launch_in, days, hours, minutes, seconds)

    override fun livestream() = context.getString(R.string.launches_livestream)
    override fun pinned() = context.getString(R.string.launches_pinned)
    override fun sortBy() = context.getString(R.string.launches_sort_by)
    override fun unpinAll() = context.getString(R.string.launches_unpin_all)
    override fun upcoming() = context.getString(R.string.launches_upcoming)
    override fun wiki() = context.getString(R.string.launches_wiki)
}
