import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class App : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("plugin.koin")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureCompose(this)
            }
        }
    }
}
