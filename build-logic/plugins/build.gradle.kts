plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}

dependencies {
    compileOnly("com.android.tools.build:gradle:7.3.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
}

gradlePlugin {
    plugins {
        register("App") {
            id = "plugin.app"
            implementationClass = "App"
        }
        register("Koin") {
            id = "plugin.koin"
            implementationClass = "Koin"
        }
        register("Feature") {
            id = "plugin.feature"
            implementationClass = "Feature"
        }
        register("Core") {
            id = "plugin.core"
            implementationClass = "Core"
        }
    }
}
