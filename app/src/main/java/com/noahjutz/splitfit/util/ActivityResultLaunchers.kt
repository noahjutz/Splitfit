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

@file:Suppress("ObjectPropertyName")

package com.noahjutz.splitfit.util

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.noahjutz.splitfit.ui.MainActivity

/**
 * Make [ActivityResultLauncher]s and their [ActivityResult]s available throughout the entire app,
 * so they don't have to be passed down through composables.
 *
 * Only use within [MainActivity]!
 */
object ActivityResultLaunchers {
    /**
     * Registers all launchers in [ActivityResultLaunchers], so they don't have to be registered
     * individually.
     */
    fun MainActivity.registerLaunchers() {
        ExportDatabase.launcher.register(this)
        ImportDatabase.launcher.register(this)
    }

    object ExportDatabase {
        private val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            type = "application/vnd.sqlite3"
            putExtra(Intent.EXTRA_TITLE, "splitfit-backup.sqlite3")
        }
        val launcher = ActivityResultLauncherHolder(intent)
    }

    object ImportDatabase {
        private val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/vnd.sqlite3"
        }
        val launcher = ActivityResultLauncherHolder(intent)
    }
}

class ActivityResultLauncherHolder(private val intent: Intent) {
    private lateinit var launcher: ActivityResultLauncher<Intent>
    fun launch() = launcher.launch(intent)

    /**
     * This method must be called in [MainActivity] when instantiating an [ActivityResultLauncherHolder]
     */
    fun register(activity: MainActivity) {
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onResult(it)
        }.also { launcher = it }
    }

    /**
     * Callback for [launcher]'s ActivityResultCallback.
     *
     * Usage:
     * launcher.onResult = { result -> doSomeOperationWith(result) }
     */
    var onResult: (ActivityResult?) -> Unit = {}
}
