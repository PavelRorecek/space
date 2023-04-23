plugins {
    id("plugin.core")
}

android {
    namespace = "com.pavelrorecek.core.design"
}

dependencies {
    api(Dependencies.Accompanist.placeholder)
    api(Dependencies.Accompanist.swiperefresh)
    api(Dependencies.Accompanist.navigation)
}
