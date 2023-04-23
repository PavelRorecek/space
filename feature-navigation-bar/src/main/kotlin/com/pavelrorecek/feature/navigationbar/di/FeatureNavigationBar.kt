package com.pavelrorecek.feature.navigationbar.di

import com.pavelrorecek.feature.navigationbar.platform.NavigationBarStringsImpl
import com.pavelrorecek.feature.navigationbar.presentation.NavigationBarStrings
import com.pavelrorecek.feature.navigationbar.presentation.NavigationBarViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val featureNavigationBar: Module = module {
    factoryOf(::NavigationBarStringsImpl) bind NavigationBarStrings::class
    viewModelOf(::NavigationBarViewModel)
}
