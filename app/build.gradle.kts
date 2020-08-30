import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(App.compileSdk)
    buildToolsVersion = "29.0.3"

    defaultConfig {
        applicationId = "com.noahjutz.gymroutines"
        minSdkVersion(App.minSdk)
        targetSdkVersion(App.targetSdk)
        versionCode = App.versionCode
        versionName = App.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = false
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerVersion = Versions.kotlin
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

configurations.all {
    resolutionStrategy {
        force("org.antlr:antlr4-runtime:4.7.1")
        force("org.antlr:antlr4-tool:4.7.1")
    }
}

dependencies {
    implementation(Libs.core)

    implementation(Libs.coroutines)

    implementation(Libs.material)

    implementation(Libs.hilt)
    kapt(Libs.hiltCompiler)
    implementation(Libs.hiltExt)
    kapt(Libs.hiltExtCompiler)

    implementation(Libs.room)
    kapt(Libs.roomCompiler)
    implementation(Libs.roomRuntime)

    testImplementation(TestLibs.junit)
    androidTestImplementation(TestLibs.junitAndroid)
    androidTestImplementation(TestLibs.espresso)

    implementation(Libs.compose)
    implementation(Libs.composeFoundation)
    implementation(Libs.composeIcons)
    implementation(Libs.composeLivedata)
    implementation(Libs.composeMaterial)
    implementation(Libs.composeTooling)
    implementation(Libs.composeRuntime)
    implementation(Libs.composeRouter)

    implementation(Libs.lifecycle)
    implementation(Libs.livedata)

    implementation(Libs.navigation)
    implementation(Libs.navigationUi)

    // Ktlint
    // TODO
}
