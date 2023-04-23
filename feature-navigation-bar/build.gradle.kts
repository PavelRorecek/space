plugins {
    id("plugin.feature")
    id("plugin.koin")
}

android {
    namespace = "com.pavelrorecek.feature.navigationBar"
}

dependencies {
    implementation(project(":core-navigation"))
}
