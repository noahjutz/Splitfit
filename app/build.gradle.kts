/*
 * Splitfit
 * Copyright (C) 2020  Noah Jutz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    kotlin("plugin.serialization") version Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlintPlugin
}

android {
    compileSdkVersion(App.compileSdk)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = App.applicationId
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
        useIR = true
    }

    lintOptions {
        textReport = true
        isAbortOnError = false
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Versions.kotlin
        kotlinCompilerExtensionVersion = Libs.Compose.version
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xallow-jvm-ir-dependencies"
        freeCompilerArgs += "-Xskip-prerelease-check"
    }
}

dependencies {
    implementation(Libs.core)

    implementation(Libs.coroutines)

    implementation(Libs.material)

    implementation(Libs.Serialization.json)

    implementation(Libs.Room.room)
    kapt(Libs.Room.compiler)
    implementation(Libs.Room.runtime)

    kapt(Libs.Lifecycle.lifecycle)
    implementation(Libs.Lifecycle.livedata)

    implementation(Libs.Navigation.navigation)
    implementation(Libs.Navigation.compose)

    testImplementation(TestLibs.junit4)
    testImplementation(TestLibs.assertJ)

    testImplementation(TestLibs.Mockk.unit)
    androidTestImplementation(TestLibs.Mockk.instrumented)

    implementation(Libs.Compose.ui)
    implementation(Libs.Compose.foundation)
    implementation(Libs.Compose.iconsCore)
    implementation(Libs.Compose.iconsExtended)
    implementation(Libs.Compose.livedata)
    implementation(Libs.Compose.material)
    implementation(Libs.Compose.tooling)
    androidTestImplementation(Libs.Compose.test)
    androidTestImplementation(Libs.Compose.testJunit4)

    implementation(Libs.Koin.android)
    implementation(Libs.Koin.viewModel)
    implementation(Libs.Koin.compose)
    testImplementation(Libs.Koin.test)
}

ktlint {
    android.set(true)
    ignoreFailures.set(true)
    disabledRules.add("no-wildcard-imports")
}
