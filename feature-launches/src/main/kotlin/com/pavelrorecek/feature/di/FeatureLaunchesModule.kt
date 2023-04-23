package com.pavelrorecek.feature.di

import com.pavelrorecek.feature.platform.LaunchesStringsImpl
import com.pavelrorecek.feature.presentation.LaunchesStrings
import com.pavelrorecek.feature.presentation.LaunchesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val featureLaunchesModule: Module = module {
    factoryOf(::LaunchesStringsImpl) bind LaunchesStrings::class
    viewModelOf(::LaunchesViewModel)
}
