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

object App {
    const val applicationId = "com.noahjutz.splitfit"
    const val compileSdk = 30
    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 8
    const val versionName = "0.1.0-dev3"
}

object Versions {
    const val serialization = "1.0.1"
    const val ktlintPlugin = "9.4.1"
    const val ktlint = "0.39.0"
    const val gradle = "7.0.0-alpha03"
    const val kotlin = "1.4.21"
    const val coroutines = "1.4.1"
    const val material = "1.2.1"
    const val lifecycle = "2.2.0"

    const val junit = "4.13.1"
    const val assertJ = "3.18.1"
}

object Libs {
    const val core = "androidx.core:core-ktx:1.3.1"

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val material = "com.google.android.material:material:${Versions.material}"

    const val serializationJson =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"

    object Room {
        private const val version = "2.2.6"
        const val room = "androidx.room:room-ktx:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val runtime = "androidx.room:room-runtime:$version"
    }

    const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val livedata = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    object Navigation {
        private const val version = "2.3.1"
        private const val versionCompose = "1.0.0-alpha04"
        const val navigation = "androidx.navigation:navigation-fragment-ktx:$version"
        const val compose = "androidx.navigation:navigation-compose:$versionCompose"
    }

    object Compose {
        const val version = "1.0.0-alpha09"
        const val ui = "androidx.compose.ui:ui:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val material = "androidx.compose.material:material:$version"
        const val iconsCore = "androidx.compose.material:material-icons-core:$version"
        const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
        const val livedata = "androidx.compose.runtime:runtime-livedata:$version"
        const val test = "androidx.compose.ui:ui-test:$version"
        const val testJunit4 = "androidx.compose.ui:ui-test-junit4:$version"
    }

    object Koin {
        private const val version = "2.2.2"
        const val android = "org.koin:koin-android:$version"
        const val viewModel = "org.koin:koin-android-viewmodel:$version"
        const val compose = "org.koin:koin-androidx-compose:$version"
        const val test = "org.koin:koin-test:$version"
    }
}

object TestLibs {
    const val junit4 = "junit:junit:${Versions.junit}"
    const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"

    object Mockk {
        private const val version = "1.10.2"
        const val unit = "io.mockk:mockk:$version"
        const val instrumented = "io.mockk:mockk-android:$version"
    }

}

object GradlePlugins {
    const val android = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}
