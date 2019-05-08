val kotlinVersion: String by settings
val androidGradlePluginVersion: String by settings

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
    resolutionStrategy {
        eachPlugin {
            val pluginId = requested.id.id
            when {
                pluginId.startsWith("com.android.") ->
                    useModule("com.android.tools.build:gradle:$androidGradlePluginVersion")

                pluginId.startsWith("org.jetbrains.kotlin.") ->
                    useVersion(kotlinVersion)
            }
        }
    }
}

rootProject.buildFileName = "build.gradle.kts"

include(":ui-testing-core")
include(":ui-testing-maps")
include(":test-app")
