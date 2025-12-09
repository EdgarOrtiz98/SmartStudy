plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // ¡ESTA LÍNEA ES CRUCIAL! Sin esto, @Serializable no funciona
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.smartstudy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smartstudy"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Tu API Key configurada correctamente
        buildConfigField("String", "TMDB_API_TOKEN", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMzY4NjEzMzEzYTdmZTE3NGE1M2U5ZDhiNjZkMWUxNSIsIm5iZiI6MTc2MDY2ODcyOC4wOTc5OTk4LCJzdWIiOiI2OGYxYWMzODk3OWQ1ZTI1NDA3NGY2MDEiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0._cJTnq2gqoNkRKrm_9FhnyTUQJbXqo5EFYC4p12eOwg\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Android Core y UI
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2") // Versión más reciente unificada
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    // Ciclo de vida y Actividad (Versiones compatibles con API 34)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // --- LIBRERÍAS DE RED Y DATOS (LIMPIAS) ---

    // Retrofit (Versión 2.11.0 para todo)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Serialización (Convertidor oficial compatible con 2.11.0)
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")

    // Motor de Serialización JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Glide (Imágenes)
    implementation("com.github.bumptech.glide:glide:4.16.0")
}