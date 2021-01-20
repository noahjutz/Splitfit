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
import androidx.lifecycle.ViewModel

class AppSettingsViewModel(
    private val application: Application,
) : ViewModel() {
    fun exportDatabase(outUri: Uri) {
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
        val inStream = application.applicationContext
            .contentResolver
            .openInputStream(inUri)

        val outStream = application.applicationContext
            .getDatabasePath("workout_routines_database")
            .outputStream()


        inStream.use { input ->
            outStream.use { output ->
                input?.copyTo(output)
            }
        }
    }
}
