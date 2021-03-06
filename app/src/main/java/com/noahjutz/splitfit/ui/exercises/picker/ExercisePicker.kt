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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.Exercise
import com.noahjutz.splitfit.ui.components.SearchBar
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ExercisePickerSheet(
    viewModel: ExercisePickerViewModel = getViewModel(),
    onExercisesSelected: (List<Exercise>) -> Unit,
    navToExerciseEditor: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            val selectedExercises by viewModel.presenter.selectedExercises.collectAsState()
            AnimatedVisibility(
                visible = selectedExercises.isNotEmpty(),
                enter = slideInHorizontally({ it * 2 }),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        onExercisesSelected(selectedExercises)
                        viewModel.editor.clearExercises()
                    }
                ) {
                    Icon(Icons.Default.Done, stringResource(R.string.pick_exercise))
                }
            }
        }
    ) {
        val allExercises by viewModel.presenter.allExercises.collectAsState(emptyList())
        LazyColumn(Modifier.fillMaxHeight()) {
            item {
                val searchQuery by viewModel.presenter.nameFilter.collectAsState()
                SearchBar(
                    modifier = Modifier.padding(16.dp),
                    value = searchQuery,
                    onValueChange = viewModel.editor::search
                )
            }

            items(allExercises.filter { !it.hidden }) { exercise ->
                val checked by viewModel.presenter.exercisesContains(exercise)
                    .collectAsState(initial = false)
                ListItem(
                    Modifier.toggleable(
                        value = checked,
                        onValueChange = {
                            if (it) viewModel.editor.addExercise(exercise)
                            else viewModel.editor.removeExercise(exercise)
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
                    modifier = Modifier.clickable(onClick = navToExerciseEditor),
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
