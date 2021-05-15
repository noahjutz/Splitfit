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

package com.noahjutz.splitfit.ui.routines.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.AppPrefs
import com.noahjutz.splitfit.ui.components.AppBarTextField
import com.noahjutz.splitfit.ui.exercises.picker.ExercisePickerSheet
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun CreateRoutineScreen(
    startWorkout: (Int) -> Unit,
    popBackStack: () -> Unit,
    routineId: Int,
    viewModel: RoutineEditorViewModel = getViewModel { parametersOf(routineId) },
    preferences: DataStore<Preferences> = get(),
) {
    val preferencesData by preferences.data.collectAsState(null)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    BackHandler(enabled = sheetState.isVisible) {
        scope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        sheetContent = {
            ExercisePickerSheet(onExercisesSelected = {
                scope.launch {
                    viewModel.editor.addExercises(it)
                    sheetState.hide()
                }
            })
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        val currentWorkout =
                            preferencesData?.get(AppPrefs.CurrentWorkout.key)
                        if (currentWorkout == null || currentWorkout < 0) {
                            startWorkout(viewModel.presenter.routine.value.routineId)
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                val snackbarResult =
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "A workout is already in progress.",
                                        "Stop current"
                                    )
                                if (snackbarResult == SnackbarResult.ActionPerformed) {
                                    preferences.edit {
                                        it[AppPrefs.CurrentWorkout.key] = -1
                                    }
                                    scaffoldState.snackbarHostState.showSnackbar("Current workout finished.")
                                }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.PlayArrow, null) },
                    text = { Text("Start Workout") },
                )
            },
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = popBackStack,
                            content = { Icon(Icons.Default.ArrowBack, null) },
                            modifier = Modifier.testTag("backButton")
                        )
                    },
                    title = {
                        var nameFieldValue by remember { mutableStateOf(viewModel.presenter.routine.value.name) }
                        AppBarTextField(
                            value = nameFieldValue,
                            onValueChange = {
                                nameFieldValue = it
                                viewModel.editor.setName(it)
                            },
                            hint = stringResource(R.string.unnamed_routine),
                        )
                    },
                )
            }
        ) {
            val setGroups by viewModel.presenter.routine
                .mapLatest { it.setGroups }
                .collectAsState(emptyList())
            LazyColumn(Modifier.fillMaxHeight(), contentPadding = PaddingValues(bottom = 70.dp)) {
                itemsIndexed(setGroups) { setGroupIndex, setGroup ->
                    val exercise = viewModel.presenter.getExercise(setGroup.exerciseId)!!
                    com.noahjutz.splitfit.ui.components.SetGroupCard(
                        name = exercise.name.takeIf { it.isNotBlank() }
                            ?: stringResource(R.string.unnamed_exercise),
                        sets = setGroup.sets,
                        onMoveDown = {
                            viewModel.editor.swapSetGroups(
                                setGroupIndex,
                                setGroupIndex + 1
                            )
                        },
                        onMoveUp = {
                            viewModel.editor.swapSetGroups(
                                setGroupIndex,
                                setGroupIndex - 1
                            )
                        },
                        onAddSet = { viewModel.editor.addSetTo(setGroup) },
                        onDeleteSet = { viewModel.editor.deleteSetFrom(setGroup, it) },
                        logReps = exercise.logReps,
                        logWeight = exercise.logWeight,
                        logTime = exercise.logTime,
                        logDistance = exercise.logDistance,
                        showCheckbox = false,
                        onDistanceChange = { setIndex, distance ->
                            viewModel.editor.updateSet(
                                setGroupIndex, setIndex,
                                distance = distance.toDoubleOrNull()
                            )
                        },
                        onRepsChange = { setIndex, reps ->
                            viewModel.editor.updateSet(
                                setGroupIndex, setIndex,
                                reps = reps.toIntOrNull()
                            )
                        },
                        onTimeChange = { setIndex, time ->
                            viewModel.editor.updateSet(
                                setGroupIndex, setIndex,
                                time = time.toIntOrNull()
                            )
                        },
                        onWeightChange = { setIndex, weight ->
                            viewModel.editor.updateSet(
                                setGroupIndex, setIndex,
                                weight = weight.toDoubleOrNull()
                            )
                        }
                    )
                }

                item {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(120.dp),
                        onClick = {
                            scope.launch {
                                sheetState.show()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(12.dp))
                        Text("Add Exercise")
                    }
                }
            }
        }
    }
}
