package com.pavelrorecek.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.pavelrorecek.core.navigation.domain.StoreCurrentScreenUseCase
import com.pavelrorecek.core.navigation.model.Screen.DAILY
import com.pavelrorecek.core.navigation.model.Screen.LAUNCHES
import com.pavelrorecek.feature.domain.LaunchesNavigationController
import com.pavelrorecek.feature.navigationbar.domain.NavigationBarNavigationController

internal class NavigationController(
    private val context: Context,
    private val storeCurrentScreen: StoreCurrentScreenUseCase,
) : LaunchesNavigationController,
    NavigationBarNavigationController {

    override fun goToDaily() {
        storeCurrentScreen(DAILY)
    }

    override fun goToLaunches() {
        storeCurrentScreen(LAUNCHES)
    }

    override fun openUrl(url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
