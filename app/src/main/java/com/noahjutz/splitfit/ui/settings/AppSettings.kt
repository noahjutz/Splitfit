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

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.noahjutz.splitfit.util.ActivityResultLaunchers
import com.noahjutz.splitfit.util.getViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun AppSettings(
    viewModel: AppSettingsViewModel = getViewModel(),
    navToAbout: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) {
        var showRestartAppDialog by remember { mutableStateOf(false) }
        ActivityResultLaunchers.ExportDatabase.launcher.onResult = { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                scope.launch {
                    if (uri != null) {
                        viewModel.exportDatabase(uri)
                    }
                    showRestartAppDialog = true
                }
            }
        }

        ActivityResultLaunchers.ImportDatabase.launcher.onResult = { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                scope.launch {
                    if (uri != null) {
                        viewModel.importDatabase(uri)
                    }
                    showRestartAppDialog = true
                }
            }
        }

        Column(Modifier.scrollable(orientation = Orientation.Vertical, state = rememberScrollState())) {
            ListItem(
                modifier = Modifier.clickable { ActivityResultLaunchers.ExportDatabase.launcher.launch() },
                text = { Text("Backup") },
                secondaryText = { Text("Save routines, exercises and workouts in a file") },
                icon = { Icon(Icons.Default.SaveAlt, null) },
            )
            ListItem(
                modifier = Modifier.clickable { ActivityResultLaunchers.ImportDatabase.launcher.launch() },
                text = { Text("Restore") },
                secondaryText = { Text("Import a database file, overriding all data.") },
                icon = { Icon(Icons.Default.SettingsBackupRestore, null) },
            )
            Divider()
            ListItem(
                modifier = Modifier.clickable(onClick = navToAbout),
                text = { Text("About Splitfit") },
                icon = { Icon(Icons.Default.Help, null) }
            )
        }

        if (showRestartAppDialog) RestartAppDialog { viewModel.restartApp() }
    }
}

@Composable
fun RestartAppDialog(
    restartApp: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        dismissButton = {},
        confirmButton = { Button(onClick = restartApp) { Text("Restart") } },
        title = { Text("Restart App") },
        text = { Text("App must be restarted after backup or restore.") }
    )
}
