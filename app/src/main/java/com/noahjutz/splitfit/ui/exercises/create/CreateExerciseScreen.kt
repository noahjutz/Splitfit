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

package com.noahjutz.splitfit.ui.exercises.create

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.ui.components.AppBarTextField
import com.noahjutz.splitfit.util.getViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@ExperimentalMaterialApi
@Composable
fun CreateExerciseScreen(
    popBackStack: () -> Unit,
    exerciseId: Int,
    viewModel: CreateExerciseViewModel = getViewModel { parametersOf(exerciseId) },
) {
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
        bodyContent = {
            var repsChecked by remember { mutableStateOf(exercise.logReps) }
            var weightChecked by remember { mutableStateOf(exercise.logWeight) }
            var timeChecked by remember { mutableStateOf(exercise.logTime) }
            var distanceChecked by remember { mutableStateOf(exercise.logDistance) }

            val alertNoLogValueSelected =
                stringResource(R.string.alert_no_log_value_selected)
            val onAnyCheckedChange = {
                presenter.exercise.value.let {
                    if (!it.logReps && !it.logWeight && !it.logTime && !it.logDistance) {
                        repsChecked = true
                        editor.updateExercise(logReps = true)
                        MainScope().launch {
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
                        text = { Text(stringResource(R.string.log_reps)) },
                        icon = {
                            Checkbox(
                                checked = repsChecked,
                                onCheckedChange = {
                                    repsChecked = it
                                    editor.updateExercise(logReps = repsChecked)
                                    onAnyCheckedChange()
                                }
                            )
                        },
                    )
                }
                item {
                    ListItem(
                        text = { Text(stringResource(R.string.log_weight)) },
                        icon = {
                            Checkbox(
                                checked = weightChecked,
                                onCheckedChange = {
                                    weightChecked = it
                                    editor.updateExercise(logWeight = weightChecked)
                                    onAnyCheckedChange()
                                }
                            )
                        },
                    )
                }
                item {
                    ListItem(
                        text = { Text(stringResource(R.string.log_time)) },
                        icon = {
                            Checkbox(
                                checked = timeChecked,
                                onCheckedChange = {
                                    timeChecked = it
                                    editor.updateExercise(logTime = timeChecked)
                                    onAnyCheckedChange()
                                }
                            )
                        },
                    )
                }
                item {
                    ListItem(
                        text = { Text(stringResource(R.string.log_distance)) },
                        icon = {
                            Checkbox(
                                checked = distanceChecked,
                                onCheckedChange = {
                                    distanceChecked = it
                                    editor.updateExercise(logDistance = distanceChecked)
                                    onAnyCheckedChange()
                                }
                            )
                        },
                    )
                }
            }
        }
    )
}
