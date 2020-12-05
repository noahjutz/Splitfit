/*
 * GymRoutines
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

package com.noahjutz.gymroutines.ui.exercises

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier

@Composable
fun ExercisesScreen(
    addEditExercise: (Int) -> Unit,
    viewModel: ExercisesViewModel
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addEditExercise(-1) },
                content = { Icon(Icons.Default.Add) }
            )
        },
        bodyContent = {
            val exercises by viewModel.exercises.observeAsState()
            LazyColumnFor(exercises ?: emptyList()) { exercise ->
                var visible by remember { mutableStateOf(!exercise.hidden) }
                if (visible) {
                    ListItem(
                        Modifier.clickable(
                            onClick = { addEditExercise(exercise.exerciseId) },
                            onLongClick = {
                                viewModel.hide(exercise, true)
                                visible = false
                            }
                        )
                    ) { Text(exercise.name) }
                }
            }
        }
    )
}
