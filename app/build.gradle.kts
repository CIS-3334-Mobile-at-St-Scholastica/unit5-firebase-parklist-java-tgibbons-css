plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    // Kotlin specific plugins are removed
}

android {
    namespace = "cis3334.java_firebase_parklist" // Updated for the Java project
    compileSdk = 36 // As per your Kotlin project

    defaultConfig {
        applicationId = "cis3334.java_firebase_parklist" // Updated for the Java project
        minSdk = 29        // As per your Kotlin project
        targetSdk = 36     // As per your Kotlin project
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
        sourceCompatibility = JavaVersion.VERSION_11 // For Java 11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // kotlinOptions are removed
    buildFeatures {
        // compose = true is removed
        viewBinding = true // Enable ViewBinding for easy view access in Java
    }
}

dependencies {
    // AndroidX Core, Lifecycle, Activity (using aliases from libs.versions.toml)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.activity)

    // UI Libraries for the View System
    implementation(libs.androidx.appcompat)
    implementation(libs.com.google.android.material)
    implementation(libs.androidx.constraintlayout)

    // Navigation for the View System (Fragments)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)

    // Standard Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose-specific dependencies are removed
}
