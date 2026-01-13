import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.prince.movietvdiscovery"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.prince.movietvdiscovery"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val watchmodeApiKey: String =
            gradleLocalProperties(project.rootDir, providers)
                .getProperty("WATCHMODE_API_KEY") ?: ""

        require(watchmodeApiKey.isNotBlank()) {
            "WATCHMODE_API_KEY is missing in local.properties"
        }

        buildConfigField(
            "String",
            "WATCHMODE_API_KEY",
            "\"$watchmodeApiKey\""
        )
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Networking
    implementation(libs.retrofit) // Retrofit Core: Handle networking
    implementation(libs.retrofit.converter.gson) // Parse API responses

    // Rx
    implementation(libs.rxkotlin) // Provides Kotlin-specific extension functions for RxJava
    implementation(libs.rxandroid) // For scheduling tasks on the Android Main Thread (UI thread)
    implementation(libs.adapter.rxjava3) // Convert Retrofit calls into RxJava Observables

    // Di
    implementation(libs.koin.android) // Core Koin for Android
    implementation(libs.koin.androidx.compose) // Koin for Compose

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Image loading
    implementation(libs.coil.compose)

    // log
    implementation(libs.logging.interceptor)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

}