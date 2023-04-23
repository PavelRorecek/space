import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
        }

        kotlinOptions {
            freeCompilerArgs += listOf(
                "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-Xopt-in=dev.chrisbanes.snapper.ExperimentalSnapperApi",
            )
        }

        dependencies {
            "implementation"("androidx.activity:activity-compose:1.6.1")
            "implementation"(platform("androidx.compose:compose-bom:2022.12.00"))
            "implementation"("androidx.compose.ui:ui")
            "implementation"("androidx.compose.ui:ui-graphics")
            "implementation"("androidx.compose.ui:ui-tooling-preview")
            "implementation"("androidx.compose.material3:material3")
            "debugImplementation"("androidx.compose.ui:ui-tooling")
            "debugImplementation"("androidx.compose.ui:ui-test-manifest")
        }
    }
}
