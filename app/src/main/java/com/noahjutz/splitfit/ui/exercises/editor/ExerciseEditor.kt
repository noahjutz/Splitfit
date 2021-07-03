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

package com.noahjutz.splitfit.ui.exercises.editor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.ui.components.TopBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalMaterialApi
@Composable
fun ExerciseEditor(
    popBackStack: () -> Unit,
    exerciseId: Int,
    viewModel: ExerciseEditorViewModel = getViewModel { parametersOf(exerciseId) },
) {
    val scope = rememberCoroutineScope()
    val editor = viewModel.Editor()
    val presenter = viewModel.Presenter()

    val exercise by presenter.exercise.collectAsState()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = { Icon(Icons.Default.ArrowBack, null) },
                    )
                },
                title = "Edit Exercise"
            )
        },
        content = {
            val alertNoLogValueSelected = stringResource(R.string.alert_no_log_value_selected)
            LaunchedEffect(
                exercise.logReps,
                exercise.logWeight,
                exercise.logTime,
                exercise.logDistance
            ) {
                if (!(exercise.logReps || exercise.logWeight || exercise.logTime || exercise.logDistance)) {
                    editor.updateExercise(logReps = true)
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(alertNoLogValueSelected)
                    }
                }
            }
            LazyColumn {
                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = exercise.name,
                        onValueChange = { editor.updateExercise(name = it) },
                        label = { Text("Exercise name") },
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.unnamed_exercise)) }
                    )
                    ListItem(
                        Modifier.toggleable(
                            value = exercise.logReps,
                            onValueChange = { editor.updateExercise(logReps = it) }
                        ),
                        text = { Text(stringResource(R.string.log_reps)) },
                        icon = { Checkbox(checked = exercise.logReps, null) },
                    )
                    ListItem(
                        Modifier.toggleable(
                            value = exercise.logWeight,
                            onValueChange = { editor.updateExercise(logWeight = it) }
                        ),
                        text = { Text(stringResource(R.string.log_weight)) },
                        icon = { Checkbox(checked = exercise.logWeight, null) },
                    )
                    ListItem(
                        Modifier.toggleable(
                            value = exercise.logTime,
                            onValueChange = { editor.updateExercise(logTime = it) }
                        ),
                        text = { Text(stringResource(R.string.log_time)) },
                        icon = { Checkbox(checked = exercise.logTime, null) },
                    )
                    ListItem(
                        Modifier.toggleable(
                            value = exercise.logDistance,
                            onValueChange = { editor.updateExercise(logDistance = it) }
                        ),
                        text = { Text(stringResource(R.string.log_distance)) },
                        icon = { Checkbox(checked = exercise.logDistance, null) },
                    )
                }
            }
        }
    )
}
