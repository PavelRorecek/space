package com.pavelrorecek.app

import android.app.Application
import com.pavelrorecek.core.navigation.di.coreNavigationModule
import com.pavelrorecek.feature.dailypicture.di.featureDailyPictureModule
import com.pavelrorecek.feature.navigationbar.di.featureNavigationBar
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

public class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)

            modules(
                appModule,
                coreNavigationModule,
                featureDailyPictureModule,
                featureNavigationBar,
            )
        }
    }
}
