package com.pavelrorecek.feature.dailypicture.di

import com.pavelrorecek.feature.dailypicture.presentation.DailyPictureViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

public val featureDailyPictureModule: Module = module {
    viewModelOf(::DailyPictureViewModel)
}
