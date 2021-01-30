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
    kotlin("plugin.serialization") version GradlePlugins.Kotlin.version
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlintPlugin
}

android {
    compileSdkVersion(App.compileSdk)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = "com.noahjutz.splitfit"
        minSdkVersion(App.minSdk)
        targetSdkVersion(App.targetSdk)
        versionCode = 17
        versionName = "0.1.0-alpha6"

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

    flavorDimensions("dimension")
    productFlavors {

        create("fdroid") {
            dimension = "dimension"
            applicationIdSuffix = ".fdroid"
        }
        create("googleplay") {
            dimension = "dimension"
            applicationIdSuffix = ".googleplay"
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = GradlePlugins.Kotlin.version
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
    implementation(Libs.Core.core)

    implementation(Libs.Coroutines.android)

    implementation(Libs.Material.material)

    implementation(Libs.Serialization.json)

    implementation(Libs.Room.room)
    kapt(Libs.Room.compiler)
    implementation(Libs.Room.runtime)

    kapt(Libs.Lifecycle.lifecycle)
    implementation(Libs.Lifecycle.livedata)

    implementation(Libs.Navigation.navigation)
    implementation(Libs.Navigation.compose)

    testImplementation(TestLibs.Junit4.junit4)
    testImplementation(TestLibs.AssertJ.assertJ)

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

    implementation(Libs.DataStore.preferences)

    implementation(Libs.ProcessPhoenix.processPhoenix)
}

ktlint {
    android.set(true)
    ignoreFailures.set(true)
    disabledRules.add("no-wildcard-imports")
}
