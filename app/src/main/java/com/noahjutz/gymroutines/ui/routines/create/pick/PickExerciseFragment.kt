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

package com.noahjutz.gymroutines.ui.routines.create.pick

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel

@Composable
fun PickExercise(
    exercisesViewModel: ExercisesViewModel,
    sharedExerciseViewModel: SharedExerciseViewModel,
    popBackStack: () -> Unit
) {
    val selectedCount = -1 // TODO: Use sharedVM.exercises.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        icon = {
                            val asset =
                                if (selectedCount == 0) Icons.Default.ArrowBack else Icons.Default.Done
                            Icon(asset)
                        }
                    )
                },
                title = {
                    Text("$selectedCount selected")
                }
            )
        },
        bodyContent = {
            val exercises by exercisesViewModel.exercises.observeAsState()
            LazyColumnFor(exercises ?: emptyList()) { exercise ->
                var checked by mutableStateOf(false)
                ListItem(
                    trailing = {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { checked = it }
                        )
                    },
                    modifier = Modifier.clickable {
                        checked = !checked
                        if (checked) sharedExerciseViewModel.add(exercise)
                        else sharedExerciseViewModel.remove(exercise)
                    }
                ) {
                    Text(exercise.name)
                }
            }
        }
    )
}
