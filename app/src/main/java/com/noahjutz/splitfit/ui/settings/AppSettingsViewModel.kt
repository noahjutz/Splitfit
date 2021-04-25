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

package com.noahjutz.splitfit.ui.settings

import android.app.Application
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakewharton.processphoenix.ProcessPhoenix
import com.noahjutz.splitfit.data.AppDatabase
import com.noahjutz.splitfit.util.DatastoreKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppSettingsViewModel(
    private val application: Application,
    private val database: AppDatabase,
    private val preferences: DataStore<androidx.datastore.preferences.core.Preferences>,
) : ViewModel() {
    private val _showBottomNavLabels = MutableStateFlow(false)
    val showBottomNavLabels = _showBottomNavLabels.asStateFlow()

    init {
        viewModelScope.launch {
            preferences.data.collectLatest {
                _showBottomNavLabels.value = it[DatastoreKeys.showBottomNavLabels] == true
            }
        }
    }

    fun setShowBottomNavLabels(value: Boolean) {
        viewModelScope.launch {
            preferences.edit {
                it[DatastoreKeys.showBottomNavLabels] = value
            }
        }
    }

    fun exportDatabase(outUri: Uri) {
        database.close()
        val inStream = application.applicationContext
            .getDatabasePath("workout_routines_database")
            .inputStream()

        val outStream = application.applicationContext
            .contentResolver
            .openOutputStream(outUri)

        inStream.use { input ->
            outStream?.use { output ->
                input.copyTo(output)
            }
        }
    }

    fun importDatabase(inUri: Uri) {
        database.close()
        val inStream = application.applicationContext
            .contentResolver
            .openInputStream(inUri)

        val databasePath = application.applicationContext
            .getDatabasePath("workout_routines_database")

        val outStream = databasePath.outputStream()

        inStream.use { input ->
            outStream.use { output ->
                input?.copyTo(output)
            }
        }
    }

    fun restartApp() = ProcessPhoenix.triggerRebirth(application.applicationContext)
}
