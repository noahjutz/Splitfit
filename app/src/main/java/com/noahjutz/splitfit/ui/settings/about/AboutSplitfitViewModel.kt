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

package com.noahjutz.splitfit.ui.settings.about

import androidx.lifecycle.ViewModel

enum class Licenses(val fullName: String) {
    APACHE2("Apache License 2.0"),
    EPL1("Eclipse Public License 1.0")
}

data class Dependency(
    val name: String,
    val license: Licenses,
    val url: String,
)

class AboutSplitfitViewModel : ViewModel() {
    val dependencies = listOf(
        Dependency("Koin", Licenses.APACHE2, "https://github.com/InsertKoinIO/koin"),
        Dependency("Android Jetpack", Licenses.APACHE2, "https://developer.android.com/jetpack"),
        Dependency("kotlinx.coroutines", Licenses.APACHE2, "https://github.com/Kotlin/kotlinx.coroutines"),
        Dependency("kotlinx.serialization", Licenses.APACHE2, "https://github.com/Kotlin/kotlinx.serialization"),
        Dependency("ProcessPhoenix", Licenses.APACHE2, "https://github.com/JakeWharton/ProcessPhoenix"),
        Dependency("Junit 4", Licenses.EPL1, "https://github.com/junit-team/junit4"),
        Dependency("AssertJ", Licenses.APACHE2, "https://github.com/assertj/assertj-core"),
        Dependency("MockK", Licenses.APACHE2, "https://github.com/mockk/mockk")
    )

    object Urls {
        const val googlePlay = "https://play.google.com/store/apps/details?id=com.noahjutz.splitfit"
        const val sourceCode = "https://github.com/noahjutz/Splitfit"
        const val donateLiberapay = "https://liberapay.com/noahjutz/donate"
        const val contributing = "https://github.com/noahjutz/Splitfit/blob/master/CONTRIBUTING.md"
    }
}
