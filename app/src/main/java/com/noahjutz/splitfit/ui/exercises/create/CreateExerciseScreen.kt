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

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focusObserver
import androidx.compose.ui.text.input.TextFieldValue

@ExperimentalFocus
@Composable
fun CreateExerciseScreen(
    popBackStack: () -> Unit,
    viewModel: CreateExerciseViewModel
) {
    val exercise by viewModel.exerciseLiveData.observeAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = { Icon(Icons.Default.ArrowBack) },
                    )
                },
                title = {
                    Box {
                        var nameFieldValue by remember {
                            mutableStateOf(
                                TextFieldValue(
                                    viewModel.exerciseLiveData.value?.name ?: "Unnamed"
                                )
                            )
                        }
                        var focusState by remember { mutableStateOf(false) }
                        BasicTextField(
                            value = nameFieldValue,
                            onValueChange = {
                                nameFieldValue = it
                                viewModel.updateExercise { name = it.text }
                            },
                            modifier = Modifier
                                .focusObserver {
                                    focusState = it.isFocused
                                }
                                .fillMaxWidth(),
                            textStyle = AmbientTextStyle.current.copy(
                                color = if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
                            ),
                            singleLine = true,
                            cursorColor = if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
                        )
                        if (nameFieldValue.text.isEmpty() && !focusState) {
                            Text("Unnamed", modifier = Modifier.drawOpacity(0.5f))
                        }
                    }
                }
            )
        },
        bodyContent = {
            ScrollableColumn {
                var repsChecked by remember { mutableStateOf(exercise!!.logReps) }
                ListItem(
                    text = { Text("Log Reps") },
                    icon = {
                        Checkbox(
                            checked = repsChecked,
                            onCheckedChange = {
                                repsChecked = it
                                viewModel.updateExercise {
                                    logReps = repsChecked
                                    if (!logWeight && !logTime && !logDistance) {
                                        logReps = true
                                        repsChecked = true
                                    } 
                                }
                            }
                        )
                    },
                )
                var weightChecked by remember { mutableStateOf(exercise!!.logWeight) }
                ListItem(
                    text = { Text("Log Weight") },
                    icon = {
                        Checkbox(
                            checked = weightChecked,
                            onCheckedChange = {
                                weightChecked = it
                                viewModel.updateExercise {
                                    logWeight = weightChecked
                                    if (!logReps && !logTime && !logDistance) {
                                        logWeight = true
                                        weightChecked = true
                                    }
                                }
                            }
                        )
                    },
                )
                var timeChecked by remember { mutableStateOf(exercise!!.logTime) }
                ListItem(
                    text = { Text("Log Time") },
                    icon = {
                        Checkbox(
                            checked = timeChecked,
                            onCheckedChange = {
                                timeChecked = it
                                viewModel.updateExercise {
                                    logTime = timeChecked
                                    if (!logWeight && !logReps && !logDistance) {
                                        logTime = true
                                        timeChecked = true
                                    }
                                }
                            }
                        )
                    },
                )
                var distanceChecked by remember { mutableStateOf(exercise!!.logDistance) }
                ListItem(
                    text = { Text("Log Distance") },
                    icon = {
                        Checkbox(
                            checked = distanceChecked,
                            onCheckedChange = {
                                distanceChecked = it
                                viewModel.updateExercise {
                                    logDistance = distanceChecked
                                    if (!logWeight && !logReps && !logTime) {
                                        logDistance = true
                                        distanceChecked = true
                                    }
                                }
                            }
                        )
                    },
                )
            }
        }
    )
}
