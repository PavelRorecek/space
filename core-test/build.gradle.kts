plugins {
    id("plugin.core")
}

android {
    namespace = "com.pavelrorecek.core.test"
}

dependencies {
    api(Dependencies.Coroutines.test)
    api(Dependencies.Kotest.core)
    api(Dependencies.junit)
    api(Dependencies.mockk)
    api(Dependencies.paparazzi)
}
