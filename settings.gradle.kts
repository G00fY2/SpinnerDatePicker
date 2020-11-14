include("app")

pluginManagement {
    val kotlinVersion: String by settings
    val androidGradlePluginVersion: String by settings
    repositories {
        google()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            when {
                requested.id.id == "com.android.application" -> useModule("com.android.tools.build:gradle:$androidGradlePluginVersion")
                requested.id.namespace == "org.jetbrains.kotlin" ->  useVersion(kotlinVersion)
            }
        }
    }
}