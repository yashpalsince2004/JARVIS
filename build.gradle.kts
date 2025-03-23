// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.5") // ✅ Use 8.5 instead of 8.11.1
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22") // ✅ Update Kotlin
    }
}
