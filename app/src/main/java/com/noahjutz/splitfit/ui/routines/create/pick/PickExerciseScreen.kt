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

package com.noahjutz.splitfit.ui.routines.create.pick

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.noahjutz.splitfit.ui.exercises.ExercisesViewModel

@Composable
fun PickExerciseScreen(
    exercisesViewModel: ExercisesViewModel,
    sharedExerciseViewModel: SharedExerciseViewModel,
    popBackStack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = { Icon(Icons.Default.ArrowBack) }
                    )
                },
                title = {
                    Text("Select Exercise")
                }
            )
        },
        bodyContent = {
            val exercises by exercisesViewModel.exercises.observeAsState()
            LazyColumnFor(exercises?.filter { !it.hidden } ?: emptyList()) { exercise ->
                var checked by remember { mutableStateOf(false) }
                onCommit(checked) {
                    if (checked) sharedExerciseViewModel.add(exercise)
                    else sharedExerciseViewModel.remove(exercise)
                }
                ListItem(
                    trailing = {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { checked = it }
                        )
                    },
                    modifier = Modifier.clickable { checked = !checked }
                ) {
                    Text(exercise.name.takeIf { it.isNotBlank() } ?: "Unnamed")
                }
            }
        }
    )
}
