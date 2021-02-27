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

package com.noahjutz.splitfit.ui.workout.editor

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.di.getViewModel
import com.noahjutz.splitfit.ui.components.AppBarTextField
import com.noahjutz.splitfit.ui.exercises.picker.SharedExercisePickerViewModel
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun WorkoutScreen(
    navToPickExercise: () -> Unit,
    popBackStack: () -> Unit,
    workoutId: Int,
    routineId: Int,
    viewModel: CreateWorkoutViewModel = getViewModel { parametersOf(workoutId, routineId) },
    sharedExercisePickerViewModel: SharedExercisePickerViewModel,
) {
    val scope = rememberCoroutineScope()

    DisposableEffect(null) {
        scope.launch {
            viewModel.editor.addExercises(sharedExercisePickerViewModel.exercises.value)
            sharedExercisePickerViewModel.clear()
        }
        onDispose {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = popBackStack) { Icon(Icons.Default.Close, null) }
                },
                title = {
                    var nameFieldValue by remember { mutableStateOf(viewModel.presenter.workout.value.name) }
                    AppBarTextField(
                        value = nameFieldValue,
                        onValueChange = {
                            nameFieldValue = it
                            viewModel.editor.setName(it)
                        },
                        hint = stringResource(R.string.unnamed_workout),
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Exercise") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = navToPickExercise,
            )
        }
    ) {
        var showFinishWorkoutDialog by remember { mutableStateOf(false) }
        if (showFinishWorkoutDialog) FinishWorkoutDialog(
            onDismiss = {
                showFinishWorkoutDialog = false
            },
            finishWorkout = {
                viewModel.editor.finishWorkout()
                popBackStack()
            }
        )
        var showCancelWorkoutDialog by remember { mutableStateOf(false) }
        if (showCancelWorkoutDialog) CancelWorkoutDialog(
            onDismiss = {
                showCancelWorkoutDialog = false
            },
            cancelWorkout = {
                scope.launch {
                    viewModel.editor.cancelWorkout()
                    popBackStack()
                }
            }
        )

        val workout by viewModel.presenter.workout.collectAsState()
        val setGroups = workout.setGroups
        LazyColumn(Modifier.fillMaxHeight()) {
            item {
                Column {
                    val duration by viewModel.presenter.durationString.collectAsState("00:00:00")
                    ListItem(
                        modifier = Modifier.clickable {},
                        text = { Text(duration) },
                        icon = { Icon(Icons.Default.AccessTime, null) },
                    )
                }

                if (setGroups.isNotEmpty()) Divider()
            }

            itemsIndexed(setGroups) { setGroupIndex, setGroup ->
                val exercise = viewModel.presenter.getExercise(setGroup.exerciseId)!!
                com.noahjutz.splitfit.ui.components.SetGroupCard(
                    name = exercise.name.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.unnamed_exercise),
                    sets = setGroup.sets,
                    onMoveDown = {
                        viewModel.editor.swapSetGroups(setGroupIndex,
                            setGroupIndex + 1)
                    },
                    onMoveUp = { viewModel.editor.swapSetGroups(setGroupIndex, setGroupIndex - 1) },
                    onAddSet = { viewModel.editor.addSetTo(setGroup) },
                    onDeleteSet = { viewModel.editor.deleteSetFrom(setGroup, it) },
                    logReps = exercise.logReps,
                    logWeight = exercise.logWeight,
                    logTime = exercise.logTime,
                    logDistance = exercise.logDistance,
                    showCheckbox = true,
                    onWeightChange = { setIndex, weight ->
                        viewModel.editor.updateSet(setGroupIndex,
                            setIndex,
                            weight = weight.toDoubleOrNull())
                    },
                    onTimeChange = { setIndex, time ->
                        viewModel.editor.updateSet(setGroupIndex,
                            setIndex,
                            time = time.toIntOrNull())
                    },
                    onRepsChange = { setIndex, reps ->
                        viewModel.editor.updateSet(setGroupIndex,
                            setIndex,
                            reps = reps.toIntOrNull())
                    },
                    onDistanceChange = { setIndex, distance ->
                        viewModel.editor.updateSet(setGroupIndex,
                            setIndex,
                            distance = distance.toDoubleOrNull())
                    },
                    onCheckboxChange = { setIndex, checked ->
                        viewModel.editor.updateSet(setGroupIndex, setIndex, complete = checked)
                    }
                )
            }

            item {
                Divider(Modifier.padding(bottom = 16.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = { showCancelWorkoutDialog = true },
                    ) {
                        Icon(Icons.Default.Delete, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Delete Workout")
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { showFinishWorkoutDialog = true }
                    ) {
                        Icon(Icons.Default.Done, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Finish Workout")
                    }
                }
                // Fix FAB overlap
                Box(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun CancelWorkoutDialog(
    onDismiss: () -> Unit,
    cancelWorkout: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Workout?") },
        text = { Text("Do you really want to delete this workout?") },
        confirmButton = { Button(onClick = cancelWorkout) { Text("Delete") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun FinishWorkoutDialog(
    onDismiss: () -> Unit,
    finishWorkout: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Finish Workout?") },
        text = { Text("Do you want to finish the workout?") },
        confirmButton = { Button(onClick = finishWorkout) { Text("Finish") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
