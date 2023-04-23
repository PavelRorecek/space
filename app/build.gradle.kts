plugins {
    id("plugin.app")
}

android {
    namespace = "com.pavelrorecek.space"

    defaultConfig {
        applicationId = "com.pavelrorecek.space"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core-design"))
    implementation(project(":core-navigation"))
    implementation(project(":feature-daily-picture"))
    implementation(project(":feature-navigation-bar"))
}
