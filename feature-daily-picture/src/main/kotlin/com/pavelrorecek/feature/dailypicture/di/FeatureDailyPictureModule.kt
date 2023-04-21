package com.pavelrorecek.feature.dailypicture.di

import com.pavelrorecek.feature.dailypicture.domain.DailyPictureRepository
import com.pavelrorecek.feature.dailypicture.data.DailyPictureRepositoryImpl
import com.pavelrorecek.feature.dailypicture.domain.LoadDailyUseCase
import com.pavelrorecek.feature.dailypicture.domain.ObserveDailyUseCase
import com.pavelrorecek.feature.dailypicture.platform.DailyStringsImpl
import com.pavelrorecek.feature.dailypicture.presentation.DailyPictureViewModel
import com.pavelrorecek.feature.dailypicture.presentation.DailyStrings
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val featureDailyPictureModule: Module = module {
    singleOf(::DailyPictureRepositoryImpl) bind DailyPictureRepository::class
    factoryOf(::LoadDailyUseCase)
    factoryOf(::ObserveDailyUseCase)
    factoryOf(::DailyStringsImpl) bind DailyStrings::class
    viewModelOf(::DailyPictureViewModel)
}
