import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.application") version "8.1.0-beta01" apply false
    id("com.android.library") version "8.1.0-beta01" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    source = files("$projectDir")
    config = files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<Detekt> {
    exclude("**/*gradle.kts")
    exclude("**/build/**")
    exclude("resources")
    exclude(".idea")
    exclude("build-logic")
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}
