import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class Koin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"("io.insert-koin:koin-core:3.3.2")
                "implementation"("io.insert-koin:koin-android:3.3.2")
                "implementation"("io.insert-koin:koin-androidx-compose:3.4.1")
            }
        }
    }
}
