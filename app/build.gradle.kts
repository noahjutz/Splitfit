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
    compileSdk = App.compileSdk
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.noahjutz.splitfit"
        minSdk = App.minSdk
        targetSdk = App.targetSdk
        versionCode = 32
        versionName = "0.1.0-alpha21"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += "room.schemaLocation" to "$projectDir/schemas"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    lint {
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

    flavorDimensions.add("dimension")
    productFlavors {

        create("fdroid") {
            dimension = "dimension"
            applicationIdSuffix = ".fdroid"
        }
        create("googleplay") {
            dimension = "dimension"
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Libs.Compose.version
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

    implementation(Libs.Activity.compose)

    implementation(Libs.Compose.ui)
    implementation(Libs.Compose.foundation)
    implementation(Libs.Compose.iconsCore)
    implementation(Libs.Compose.iconsExtended)
    implementation(Libs.Compose.runtimeLivedata)
    implementation(Libs.Compose.material)
    implementation(Libs.Compose.tooling)
    androidTestImplementation(Libs.Compose.test)
    androidTestImplementation(Libs.Compose.testJunit4)

    implementation(Libs.Koin.android)
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
