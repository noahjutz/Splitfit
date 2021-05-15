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

package com.noahjutz.splitfit.ui.exercises.picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.Exercise
import com.noahjutz.splitfit.ui.components.SearchTopBar
import org.koin.androidx.compose.getViewModel


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
// Experimental
fun ExercisePickerSheet(
    viewModel: ExercisePickerViewModel = getViewModel(),
    sharedExercisePickerViewModel: SharedExercisePickerViewModel = getViewModel(),
    onExercisesSelected: (List<Exercise>) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            val selectedExercises by sharedExercisePickerViewModel.exercises.collectAsState()
            AnimatedVisibility(
                visible = selectedExercises.isNotEmpty(),
                enter = slideInHorizontally({ it * 2 }),
                exit = fadeOut()
            ) {
                FloatingActionButton(onClick = {
                    onExercisesSelected(selectedExercises)
                    sharedExercisePickerViewModel.clear()
                }) {
                    Icon(Icons.Default.Done, stringResource(R.string.pick_exercise))
                }
            }
        }
    ) {
        val allExercises by viewModel.exercises.collectAsState(emptyList())
        LazyColumn(Modifier.fillMaxHeight()) {
            items(allExercises.filter { !it.hidden }) { exercise ->
                val checked by sharedExercisePickerViewModel.contains(exercise)
                    .collectAsState(initial = false)
                ListItem(
                    Modifier.toggleable(
                        value = checked,
                        onValueChange = {
                            if (it) sharedExercisePickerViewModel.add(exercise)
                            else sharedExercisePickerViewModel.remove(exercise)
                        }
                    ),
                    icon = { Checkbox(checked = checked, onCheckedChange = null) },
                ) {
                    Text(
                        exercise.name.takeIf { it.isNotBlank() }
                            ?: stringResource(R.string.unnamed_exercise)
                    )
                }
            }

            item {
                ListItem(
                    modifier = Modifier.clickable {},
                    icon = { Icon(Icons.Default.Add, null, tint = colors.primary) },
                    text = { Text(stringResource(R.string.new_exercise), color = colors.primary) },
                )
            }

            item {
                // Fix FAB overlap
                Box(Modifier.height(72.dp))
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ExercisePicker(
    viewModel: ExercisePickerViewModel = getViewModel(),
    sharedExercisePickerViewModel: SharedExercisePickerViewModel,
    popBackStack: () -> Unit,
) {
    var save by remember { mutableStateOf(false) }
    DisposableEffect(null) {
        onDispose {
            if (!save) sharedExercisePickerViewModel.clear()
        }
    }

    Scaffold(
        topBar = {
            var searchQuery by remember { mutableStateOf("") }
            SearchTopBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = { Icon(Icons.Default.Close, null) }
                    )
                },
                title = stringResource(R.string.pick_exercise),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.search(it)
                },
            )
        },
        floatingActionButton = {
            val selectedExercises by sharedExercisePickerViewModel.exercises.collectAsState()
            AnimatedVisibility(
                visible = selectedExercises.isNotEmpty(),
                enter = slideInHorizontally({ it * 2 }),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        save = true
                        popBackStack()
                    }
                ) {
                    Icon(Icons.Default.Done, stringResource(R.string.pick_exercise))
                }
            }
        }
    ) {
        val exercises by viewModel.exercises.collectAsState(emptyList())
        LazyColumn(Modifier.fillMaxHeight()) {
            items(exercises.filter { !it.hidden }) { exercise ->
                val checked by sharedExercisePickerViewModel.contains(exercise)
                    .collectAsState(initial = false)
                ListItem(
                    Modifier.toggleable(
                        value = checked,
                        onValueChange = {
                            if (it) sharedExercisePickerViewModel.add(exercise)
                            else sharedExercisePickerViewModel.remove(exercise)
                        }
                    ),
                    icon = { Checkbox(checked = checked, onCheckedChange = null) },
                ) {
                    Text(
                        exercise.name.takeIf { it.isNotBlank() }
                            ?: stringResource(R.string.unnamed_exercise)
                    )
                }
            }

            item {
                ListItem(
                    modifier = Modifier.clickable {},
                    icon = { Icon(Icons.Default.Add, null, tint = colors.primary) },
                    text = { Text(stringResource(R.string.new_exercise), color = colors.primary) },
                )
            }

            item {
                // Fix FAB overlap
                Box(Modifier.height(72.dp))
            }
        }
    }
}
