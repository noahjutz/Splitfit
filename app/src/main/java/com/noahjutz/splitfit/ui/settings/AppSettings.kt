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

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalMaterialApi
@Composable
fun AppSettings(
    viewModel: AppSettingsViewModel = getViewModel(),
    navToAbout: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) {
        var showRestartAppDialog by remember { mutableStateOf(false) }
        var showResetSettingsDialog by remember { mutableStateOf(false) }

        val exportDatabaseLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
                scope.launch {
                    if (uri != null) {
                        viewModel.exportDatabase(uri)
                        showRestartAppDialog = true
                    }
                }
            }

        val importDatabaseLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                if (uri != null) {
                    viewModel.importDatabase(uri)
                    showRestartAppDialog = true
                }
            }

        Column(
            Modifier.scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollState()
            )
        ) {
            ListItem(
                modifier = Modifier.clickable {
                    val now = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val nowFormatted = formatter.format(now)
                    exportDatabaseLauncher.launch("splitfit_$nowFormatted.db")
                },
                text = { Text("Backup") },
                secondaryText = { Text("Save routines, exercises and workouts in a file") },
                icon = { Icon(Icons.Default.SaveAlt, null) },
            )
            ListItem(
                modifier = Modifier.clickable { importDatabaseLauncher.launch(null) },
                text = { Text("Restore") },
                secondaryText = { Text("Import a database file, overriding all data.") },
                icon = { Icon(Icons.Default.SettingsBackupRestore, null) },
            )
            Divider()
            val settingShowBottomNavLabels by viewModel.showBottomNavLabels.collectAsState()
            ListItem(
                modifier = Modifier.toggleable(
                    value = settingShowBottomNavLabels,
                    onValueChange = { viewModel.setShowBottomNavLabels(it) }
                ),
                text = { Text("Show bottom navigation labels") },
                trailing = {
                    Checkbox(
                        checked = settingShowBottomNavLabels,
                        onCheckedChange = null
                    )
                },
                icon = {}
            )
            ListItem(
                modifier = Modifier.clickable(onClick = { showResetSettingsDialog = true }),
                text = { Text("Reset all settings") },
                icon = {},
            )
            Divider()
            ListItem(
                modifier = Modifier.clickable(onClick = navToAbout),
                text = { Text("About Splitfit") },
                icon = { Icon(Icons.Default.Help, null) }
            )
        }

        if (showRestartAppDialog) RestartAppDialog(restartApp = viewModel::restartApp)
        if (showResetSettingsDialog) ResetSettingsDialog(
            onDismiss = { showResetSettingsDialog = false },
            resetSettings = {
                showResetSettingsDialog = false
                viewModel.resetSettings()
            }
        )
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

@Composable
fun ResetSettingsDialog(
    onDismiss: () -> Unit,
    resetSettings: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = { TextButton(onClick = onDismiss) { Text("Dismiss") } },
        confirmButton = { Button(onClick = resetSettings) { Text("Reset all settings") } },
        title = { Text("Reset all settings?") },
        text = { Text("Are you sure you want to reset all settings to their default values?") }
    )
}