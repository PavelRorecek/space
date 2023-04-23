package com.pavelrorecek.core.navigation.di

import com.pavelrorecek.core.navigation.data.CurrentScreenRepositoryImpl
import com.pavelrorecek.core.navigation.domain.CurrentScreenRepository
import com.pavelrorecek.core.navigation.domain.ObserveCurrentScreenUseCase
import com.pavelrorecek.core.navigation.domain.StoreCurrentScreenUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val coreNavigationModule: Module = module {
    singleOf(::CurrentScreenRepositoryImpl) bind CurrentScreenRepository::class
    factoryOf(::StoreCurrentScreenUseCase)
    factoryOf(::ObserveCurrentScreenUseCase)
}
