package com.pavelrorecek.feature.navigationbar.platform

import android.content.Context
import com.pavelrorecek.feature.navigationBar.R
import com.pavelrorecek.feature.navigationbar.presentation.NavigationBarStrings

internal class NavigationBarStringsImpl(
    private val context: Context,
) : NavigationBarStrings {

    override fun daily() = context.getString(R.string.navigationbar_daily)
    override fun launches() = context.getString(R.string.navigationbar_launches)
}
