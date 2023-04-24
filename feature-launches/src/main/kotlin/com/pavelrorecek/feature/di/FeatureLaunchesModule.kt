package com.pavelrorecek.feature.di

import androidx.room.Room
import com.pavelrorecek.feature.data.LaunchesApi
import com.pavelrorecek.feature.data.LaunchesRepositoryImpl
import com.pavelrorecek.feature.data.PinnedLaunchesDatabase
import com.pavelrorecek.feature.domain.DeletePinnedLaunchUseCase
import com.pavelrorecek.feature.domain.DeleteAllPinnedLaunchesUseCase
import com.pavelrorecek.feature.domain.LaunchesRepository
import com.pavelrorecek.feature.domain.ObserveLaunchesUseCase
import com.pavelrorecek.feature.domain.ObservePinnedLaunchesUseCase
import com.pavelrorecek.feature.domain.RequestLaunchesUseCase
import com.pavelrorecek.feature.domain.StorePinnedLaunchUseCase
import com.pavelrorecek.feature.platform.LaunchesStringsImpl
import com.pavelrorecek.feature.presentation.LaunchesStrings
import com.pavelrorecek.feature.presentation.LaunchesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public val featureLaunchesModule: Module = module {
    factory {
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()

        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://api.spacexdata.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(LaunchesApi::class.java)
    }
    factory {
        Room.databaseBuilder(get(), PinnedLaunchesDatabase::class.java, "PinnedLaunchesDatabase").build()
    }
    factory { get<PinnedLaunchesDatabase>().pinnedLaunchesDao() }
    singleOf(::LaunchesRepositoryImpl) bind LaunchesRepository::class
    factoryOf(::RequestLaunchesUseCase)
    factoryOf(::ObserveLaunchesUseCase)
    factoryOf(::ObservePinnedLaunchesUseCase)
    factoryOf(::StorePinnedLaunchUseCase)
    factoryOf(::DeletePinnedLaunchUseCase)
    factoryOf(::DeleteAllPinnedLaunchesUseCase)
    factoryOf(::LaunchesStringsImpl) bind LaunchesStrings::class
    viewModelOf(::LaunchesViewModel)
}
