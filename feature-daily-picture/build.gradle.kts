plugins {
    id("plugin.feature")
    id("plugin.koin")
    id("app.cash.paparazzi") version "1.2.0"
    kotlin("plugin.serialization") version "1.8.20"
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.pavelrorecek.feature.dailyPicture"
}

dependencies {
    implementation(project(":core-design"))
    implementation(Dependencies.Serialization.json)
    implementation(Dependencies.coil)
    implementation(Dependencies.datetime)
    implementation(Dependencies.Retrofit.core)
    implementation(Dependencies.Retrofit.gson)
    implementation(Dependencies.Retrofit.interceptor)

    val room_version = "2.5.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")


    testImplementation(project(":core-test"))
}
