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

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.ui.components.AppBarTextField
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
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = { Icon(Icons.Default.ArrowBack, null) },
                    )
                },
                title = {
                    var nameFieldValue by remember { mutableStateOf(exercise.name) }
                    AppBarTextField(
                        value = nameFieldValue,
                        onValueChange = {
                            nameFieldValue = it
                            editor.updateExercise(name = it)
                        },
                        hint = stringResource(R.string.unnamed_exercise),
                    )
                }
            )
        },
        content = {
            val alertNoLogValueSelected = stringResource(R.string.alert_no_log_value_selected)
            val onAnyCheckedChange = {
                presenter.exercise.value.let {
                    if (!it.logReps && !it.logWeight && !it.logTime && !it.logDistance) {
                        editor.updateExercise(logReps = true)
                        scope.launch {
                            scaffoldState.snackbarHostState.let {
                                it.currentSnackbarData?.dismiss()
                                it.showSnackbar(alertNoLogValueSelected)
                            }
                        }
                    }
                }
            }
            LazyColumn {
                item {
                    ListItem(
                        Modifier.toggleable(
                            value = exercise.logReps,
                            onValueChange = {
                                editor.updateExercise(logReps = it)
                                onAnyCheckedChange()
                            }
                        ),
                        text = { Text(stringResource(R.string.log_reps)) },
                        icon = {
                            Checkbox(
                                checked = exercise.logReps,
                                onCheckedChange = null,
                            )
                        },
                    )
                }
                item {
                    ListItem(
                        Modifier.toggleable(
                            value = exercise.logWeight,
                            onValueChange = {
                                editor.updateExercise(logWeight = it)
                                onAnyCheckedChange()
                            }
                        ),
                        text = { Text(stringResource(R.string.log_weight)) },
                        icon = {
                            Checkbox(
                                checked = exercise.logWeight,
                                onCheckedChange = null,
                            )
                        },
                    )
                }
                item {
                    ListItem(
                        Modifier.toggleable(
                            value = exercise.logTime,
                            onValueChange = {
                                editor.updateExercise(logTime = it)
                                onAnyCheckedChange()
                            }
                        ),
                        text = { Text(stringResource(R.string.log_time)) },
                        icon = {
                            Checkbox(
                                checked = exercise.logTime,
                                onCheckedChange = null,
                            )
                        },
                    )
                }
                item {
                    ListItem(
                        Modifier.toggleable(
                            value = exercise.logDistance,
                            onValueChange = {
                                editor.updateExercise(logDistance = it)
                                onAnyCheckedChange()
                            }
                        ),
                        text = { Text(stringResource(R.string.log_distance)) },
                        icon = {
                            Checkbox(
                                checked = exercise.logDistance,
                                onCheckedChange = null,
                            )
                        },
                    )
                }
            }
        }
    )
}
