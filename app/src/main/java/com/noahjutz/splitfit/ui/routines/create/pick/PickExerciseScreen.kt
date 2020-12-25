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

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import org.koin.androidx.compose.getViewModel
import org.koin.core.context.GlobalContext.get

@ExperimentalAnimationApi
@Composable
fun PickExerciseScreen(
    viewModel: PickExerciseViewModel = getViewModel(),
    sharedExerciseViewModel: SharedExerciseViewModel,
    popBackStack: () -> Unit,
) {
    var save = false
    onDispose {
        if (!save) sharedExerciseViewModel.clear()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = { Icon(Icons.Default.Close) }
                    )
                },
                title = {
                    Text(stringResource(R.string.pick_exercise))
                }
            )
        },
        floatingActionButton = {
            val selectedExercises by sharedExerciseViewModel.exercises.collectAsState()
            AnimatedVisibility(
                visible = selectedExercises.isNotEmpty(),
                enter = slideInHorizontally({ it * 2 }),
                exit = fadeOut()
            ) {
                FloatingActionButton(onClick = { save = true; popBackStack() }) {
                    Icon(Icons.Default.Done)
                }
            }
        }
    ) {
        val exercises by viewModel.exercises.observeAsState()
        LazyColumn(Modifier.fillMaxHeight()) {
            items(exercises?.filter { !it.hidden } ?: emptyList()) { exercise ->
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
                    Text(
                        exercise.name.takeIf { it.isNotBlank() } ?: stringResource(
                            R.string.unnamed_exercise
                        )
                    )
                }
            }
            item {
                // Fix FAB overlap
                Box(Modifier.height(72.dp)) {}
            }
        }
    }
}
