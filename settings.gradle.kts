pluginManagement {
    repositories {
        google()  // ✅ Add this
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()  // ✅ Add this
        mavenCentral()
    }
}



rootProject.name = "Jarvis"
include(":app")
