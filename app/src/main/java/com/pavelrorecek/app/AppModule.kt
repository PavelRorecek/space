package com.pavelrorecek.app

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    viewModelOf(::MainViewModel)
}
