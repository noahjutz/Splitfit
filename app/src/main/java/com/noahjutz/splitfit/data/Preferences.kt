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

package com.noahjutz.splitfit.data

import android.content.Context
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.noahjutz.splitfit.R

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ColorTheme(
    @StringRes val themeName: Int
) {
    FollowSystem(R.string.theme_follow_system),
    Light(R.string.theme_light),
    Dark(R.string.theme_dark),
    Black(R.string.theme_black)
}

sealed class AppPrefs<T>(val key: Preferences.Key<T>, val defaultValue: T) {
    object IsFirstRun : AppPrefs<Boolean>(booleanPreferencesKey("isFirstRun"), false)
    object CurrentWorkout : AppPrefs<Int>(intPreferencesKey("currentWorkout"), -1)

    object ShowBottomNavLabels :
        AppPrefs<Boolean>(booleanPreferencesKey("showBottomNavLabels"), true)

    object AppTheme : AppPrefs<String>(stringPreferencesKey("appTheme"), ColorTheme.FollowSystem.name)
}

suspend fun DataStore<Preferences>.resetAppSettings() {
    edit {
        it[AppPrefs.ShowBottomNavLabels.key] = AppPrefs.ShowBottomNavLabels.defaultValue
        it[AppPrefs.IsFirstRun.key] = AppPrefs.IsFirstRun.defaultValue
        it[AppPrefs.AppTheme.key] = AppPrefs.AppTheme.defaultValue
    }
}
