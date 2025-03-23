import org.gradle.api.JavaVersion


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id("java")
    //id("org.junit.platform.gradle.plugin") version "1.9.3" // Add JUnit 5 Plugin
}
android {
    namespace = "com.example.jarvis"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jarvis"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    testOptions {
        unitTests.all {
           //useJUnitPlatform()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}


dependencies {
    implementation("ai.picovoice:porcupine-android:2.2.0")  // ✅ Correct Version
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")

    //testImplementation("junit:junit:4.13.2")
    // JUnit 5 API
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")

    // JUnit 5 Engine (for running tests)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testImplementation ("org.junit.jupiter:junit-jupiter-api")

    // Optional: Mockito for mocking (if needed)
    testImplementation("org.mockito:mockito-core:5.5.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}