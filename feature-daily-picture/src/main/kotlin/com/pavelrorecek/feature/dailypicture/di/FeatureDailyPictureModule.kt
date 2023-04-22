package com.pavelrorecek.feature.dailypicture.di

import androidx.room.Room
import com.pavelrorecek.feature.dailypicture.data.DailyApi
import com.pavelrorecek.feature.dailypicture.data.DailyDatabase
import com.pavelrorecek.feature.dailypicture.data.DailyPictureRepositoryImpl
import com.pavelrorecek.feature.dailypicture.domain.DailyPictureRepository
import com.pavelrorecek.feature.dailypicture.domain.ObserveDailyUseCase
import com.pavelrorecek.feature.dailypicture.domain.RequestDailyUseCase
import com.pavelrorecek.feature.dailypicture.platform.DailyStringsImpl
import com.pavelrorecek.feature.dailypicture.presentation.DailyStrings
import com.pavelrorecek.feature.dailypicture.presentation.DailyViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public val featureDailyPictureModule: Module = module {
    factory {
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val url = chain.request().url.newBuilder()
                    .addQueryParameter("api_key", "DEMO_KEY")
                    .build()

                val request: Request = chain.request().newBuilder().url(url).build()
                chain.proceed(request)
            }

            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()

        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DailyApi::class.java)
    }
    factory {
        Room.databaseBuilder(get(), DailyDatabase::class.java, "DailyDatabase").build()
    }
    factory { get<DailyDatabase>().dailyDao() }
    singleOf(::DailyPictureRepositoryImpl) bind DailyPictureRepository::class
    factoryOf(::RequestDailyUseCase)
    factoryOf(::ObserveDailyUseCase)
    factoryOf(::DailyStringsImpl) bind DailyStrings::class
    viewModelOf(::DailyViewModel)
}
