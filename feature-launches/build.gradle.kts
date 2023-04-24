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

    implementation(Dependencies.Room.runtime)
    annotationProcessor(Dependencies.Room.compiler)
    ksp(Dependencies.Room.compiler)
    implementation(Dependencies.Room.ktx)

    testImplementation(project(":core-test"))
}
