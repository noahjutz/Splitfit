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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

sealed class AppPrefs<T>(val key: Preferences.Key<T>, val defaultValue: T) {
    object IsFirstRun : AppPrefs<Boolean>(booleanPreferencesKey("isFirstRun"), false)
    object CurrentWorkout : AppPrefs<Int>(intPreferencesKey("currentWorkout"), -1)

    object ShowBottomNavLabels :
        AppPrefs<Boolean>(booleanPreferencesKey("showBottomNavLabels"), true)
}

suspend fun DataStore<Preferences>.resetAppSettings() {
    edit {
        it[AppPrefs.ShowBottomNavLabels.key] = AppPrefs.ShowBottomNavLabels.defaultValue
        it[AppPrefs.IsFirstRun.key] = AppPrefs.IsFirstRun.defaultValue
    }
}