package com.pavelrorecek.feature.dailypicture.platform

import android.content.Context
import com.pavelrorecek.feature.dailyPicture.R
import com.pavelrorecek.feature.dailypicture.presentation.DailyStrings

internal class DailyStringsImpl(
    private val context: Context,
) : DailyStrings {

    override fun explanation() = context.getString(R.string.dailypicture_explanation)
    override fun today() = context.getString(R.string.dailypicture_today)
}
