package com.pavelrorecek.app

import com.pavelrorecek.core.navigation.domain.StoreCurrentScreenUseCase
import com.pavelrorecek.core.navigation.model.Screen.DAILY
import com.pavelrorecek.core.navigation.model.Screen.LAUNCHES
import com.pavelrorecek.feature.navigationbar.domain.NavigationBarNavigationController

internal class NavigationController(
    private val storeCurrentScreen: StoreCurrentScreenUseCase,
) : NavigationBarNavigationController {

    override fun goToDaily() {
        storeCurrentScreen(DAILY)
    }

    override fun goToLaunches() {
        storeCurrentScreen(LAUNCHES)
    }
}
