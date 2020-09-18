object App {
    const val compileSdk = 30
    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 5
    const val versionName = "0.1.0"
}

object Versions {
    const val gradle = "4.2.0-alpha11"
    const val kotlin = "1.4.0"
    const val hilt = "2.28-alpha"
    const val hiltExt = "1.0.0-alpha02"
    const val room = "2.2.5"
    const val coroutines = "1.3.6"
    const val compose = "1.0.0-alpha03"
    const val composeRouter = "0.18.0"
    const val material = "1.2.0"
    const val lifecycle = "2.2.0"
    const val navigation = "2.3.0"
}

object Libs {
    const val core = "androidx.core:core-ktx:1.3.1"

    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val material = "com.google.android.material:material:${Versions.material}"

    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltExt = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltExt}"
    const val hiltExtCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltExt}"

    const val room = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"

    const val compose = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeTooling = "androidx.ui:ui-tooling:${Versions.compose}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeLivedata = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.compose}"
    const val composeIcons = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val composeRouter = "com.github.zsoltk:compose-router:${Versions.composeRouter}"

    const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val livedata = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

}

object TestLibs {

}

object Classpaths {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val hiltGradle = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}