package com.pavelrorecek.app

import com.pavelrorecek.feature.domain.LaunchesNavigationController
import com.pavelrorecek.feature.navigationbar.domain.NavigationBarNavigationController
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.binds
import org.koin.dsl.module

internal val appModule = module {
    factoryOf(::NavigationController) binds arrayOf(
        LaunchesNavigationController::class,
        NavigationBarNavigationController::class,
    )
    viewModelOf(::MainViewModel)
}
