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
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
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
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Versions.kotlin
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = (freeCompilerArgs as? MutableList ?: mutableListOf()).apply {
            add("-Xallow-jvm-ir-dependencies")
            add("-Xskip-prerelease-check")
        }.toList()
    }
}

dependencies {
    implementation(Libs.core)

    implementation(Libs.coroutines)

    implementation(Libs.material)

    implementation(Libs.gson)

    implementation(Libs.hilt)
    kapt(Libs.hiltCompiler)
    implementation(Libs.hiltExt)
    kapt(Libs.hiltExtCompiler)

    implementation(Libs.roomKtx)
    kapt(Libs.roomCompiler)
    implementation(Libs.roomRuntime)

    implementation(Libs.lifecycle)
    implementation(Libs.livedata)

    implementation(Libs.navigation)
    implementation(Libs.navigationUi)

    testImplementation(TestLibs.junit4)
    testImplementation(TestLibs.assertJ)
    testImplementation(TestLibs.mockkUnit)
    androidTestImplementation(TestLibs.mockkInstrumented)
    androidTestImplementation(TestLibs.composeTest)
    androidTestImplementation(TestLibs.composeTestJunit4)

    implementation(Libs.compose)
    implementation(Libs.composeFoundation)
    implementation(Libs.composeIconsCore)
    implementation(Libs.composeIconsExt)
    implementation(Libs.composeLivedata)
    implementation(Libs.composeMaterial)
    implementation(Libs.composeTooling)
    implementation(Libs.composeNavigation)

    implementation(Libs.koin)
    testImplementation(TestLibs.koinTest)
}

ktlint {
    android.set(true)
    ignoreFailures.set(true)
    disabledRules.add("no-wildcard-imports")
}
