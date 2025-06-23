plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin") // Se você usa Safe Args com Navigation Components XML
    id("com.google.devtools.ksp")version "2.0.21-1.0.28"
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.nonato.maintenancereportapp"
    compileSdk = 35 // Considere usar 34 se 35 for preview e estiver causando problemas
    defaultConfig {
        minSdk = 21
        targetSdk = 35 // Considere usar 34 se 35 for preview
        versionCode = 1
        versionName = "1.0"
        // ...
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
        // viewBinding = true // Adicione se você usa View Binding com XML
    }

    sourceSets {
        getByName("main") {
            java {
                srcDirs(
                    "build/generated/ksp/main/kotlin",
                    "build/generated/ksp/debug/kotlin" // opcional para builds debug
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    // Jetpack Core e Lifecycle (usando libs do TOML)
    implementation(libs.androidx.core.ktx) // Já inclui o core-ktx
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose) // Para integração de Activity com Compose

    // Compose (usando BOM do TOML)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Preferences (usando libs do TOML)
    implementation(libs.androidx.preference)

    // Testes (usando libs do TOML)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Para testes de UI com Compose
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling) // Para ferramentas de debug do Compose
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dependências que não estavam no TOML ou eram duplicadas (verifique se ainda são necessárias)
    // Se você usa Views XML tradicionais com AppCompat e Material Design Components:
    implementation("androidx.appcompat:appcompat:1.6.1") // Para temas AppCompat, etc.
    implementation("com.google.android.material:material:1.12.0") // Versão estável mais recente
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Se você usa ConstraintLayout em XML

    // Navigation Component (para navegação baseada em Fragmentos/XML)
    // Se você usa Compose Navigation, estas podem não ser necessárias ou seriam diferentes.
    val navVersion = "2.7.7" // Versão estável mais recente
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // Room (Android Jetpack) - Usando versão estável
    val roomVersion = "2.7.0" // Versão estável recomendada
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // ViewModel and LiveData (AndroidX Lifecycle)
    val lifecycleVersion = "2.8.3" // Versão estável mais recente
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // CameraX
    val cameraxVersion = "1.3.4" // Versão estável mais recente (verifique a última)
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion") // Para PreviewView, use a mesma cameraxVersion

    // Outras dependências que você usa com Views XML
    implementation("androidx.recyclerview:recyclerview:1.3.2") // Versão estável mais recente
    implementation("androidx.cardview:cardview:1.0.0") // Já é uma versão antiga, mas estável
}