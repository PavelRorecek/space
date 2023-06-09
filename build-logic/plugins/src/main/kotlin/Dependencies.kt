object Dependencies {

    object Accompanist {
        private const val version = "0.31.0-alpha"

        const val navigation = "com.google.accompanist:accompanist-navigation-animation:$version"

        // Remove Accompanist swiperefresh once pullRefresh makes it into material3
        const val swiperefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
        const val placeholder = "com.google.accompanist:accompanist-placeholder-material:$version"
    }

    const val coil = "io.coil-kt:coil-compose:2.3.0"

    object Compose {
        const val compilerVersion = "1.4.6"
    }

    const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"

    object Coroutines {
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"
    }

    const val junit = "junit:junit:4.13.2"

    object Kotest {
        const val core = "io.kotest:kotest-assertions-core:5.5.4"
    }

    const val mockk = "io.mockk:mockk:1.13.3"
    const val paparazzi = "app.cash.paparazzi:paparazzi-gradle-plugin:1.2.0"

    object Retrofit {
        private const val version = "2.9.0"

        const val core = "com.squareup.retrofit2:retrofit:$version"
        const val gson = "com.squareup.retrofit2:converter-gson:$version"
        const val interceptor = "com.squareup.okhttp3:logging-interceptor:4.10.0"
    }

    object Room {
        private const val room_version = "2.5.1"

        const val runtime = "androidx.room:room-runtime:$room_version"
        const val compiler = "androidx.room:room-compiler:$room_version"
        const val ktx = "androidx.room:room-ktx:$room_version"
    }

    object Serialization {
        const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
    }
}
