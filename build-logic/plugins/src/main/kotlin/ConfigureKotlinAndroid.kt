import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = 33

        defaultConfig {
            minSdk = 24
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            freeCompilerArgs += listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xexplicit-api=strict",
            )
        }
    }
}
