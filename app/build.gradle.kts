import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("plugin.serialization") version "2.3.0"
}

android {
    namespace = "com.prince.cinemon"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.prince.cinemon"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {

        debug {
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

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            buildConfigField(
                "String",
                "WATCHMODE_API_KEY",
                "\"\""
            )

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
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
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

    testImplementation(libs.mockk)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

}