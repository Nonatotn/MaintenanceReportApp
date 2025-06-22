plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)

    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.nonato.maintenancereportapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nonato.maintenancereportapp"
        minSdk = 31
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0")

    // Room (Android Jetpack)
    val roomVersion = "2.7.1"
    implementation ("androidx.room:room-runtime:$roomVersion")
    ksp ("androidx.room:room-compiler:$roomVersion")
    implementation ("androidx.room:room-ktx:$roomVersion")

    // Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.0")

    // ViewModel and LiveData (AndroidX Lifecycle)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // CameraX
    val camerax_version = "1.3.0" // Mudou def para val
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:1.3.0")      // para PreviewView

    // Outras
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.cardview:cardview:1.0.0")

}