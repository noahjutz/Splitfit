/*
 * GymRoutines
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

object App {
    const val compileSdk = 30
    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 5
    const val versionName = "0.1.0"
}

object Versions {
    const val gson = "2.8.6"
    const val ktlintPlugin = "9.4.0"
    const val ktlint = "0.39.0"
    const val gradle = "4.2.0-alpha14"
    const val kotlin = "1.4.10"
    const val hilt = "2.28-alpha"
    const val hiltExt = "1.0.0-alpha02"
    const val room = "2.2.5"
    const val coroutines = "1.3.6"
    const val material = "1.2.0"
    const val lifecycle = "2.2.0"
    const val navigation = "2.3.0"
    const val compose = "1.0.0-alpha04"

    const val junit = "4.12"
    const val assertJ = "3.11.1"
    const val mockK = "1.10.2"
}

object Libs {
    const val core = "androidx.core:core-ktx:1.3.1"

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val material = "com.google.android.material:material:${Versions.material}"

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltExt = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltExt}"
    const val hiltExtCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltExt}"

    const val room = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"

    const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val livedata = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    const val compose = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeTooling = "androidx.ui:ui-tooling:${Versions.compose}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeIconsCore = "androidx.compose.material:material-icons-core:${Versions.compose}"
    const val composeIconsExt = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val composeLivedata = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
    const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
    const val mockkUnit = "io.mockk:mockk:${Versions.mockK}"
    const val mockkInstrumented = "io.mockk:mockk-android:${Versions.mockK}"
    const val composeTest = "androidx.ui:ui-test:${Versions.compose}"
}

object Classpaths {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val hiltGradle = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}
