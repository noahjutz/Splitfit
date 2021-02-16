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

package com.noahjutz.splitfit.ui.exercises

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.ui.components.SearchTopBar
import com.noahjutz.splitfit.ui.components.SwipeToDeleteBackground
import com.noahjutz.splitfit.util.getViewModel

@ExperimentalMaterialApi
@Composable
fun ExerciseList(
    addEditExercise: (Int) -> Unit,
    viewModel: ExerciseListViewModel = getViewModel(),
) {
    Scaffold(
        topBar = {
            var searchQuery by remember { mutableStateOf("") }
            SearchTopBar(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.search(it)
                },
                title = stringResource(R.string.tab_exercises),
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { addEditExercise(viewModel.addExercise().toInt()) },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("New Exercise") },
            )
        },
        bodyContent = {
            val exercises by viewModel.exercises.collectAsState(emptyList())
            LazyColumn(Modifier.fillMaxHeight()) {
                items(exercises.filter { !it.hidden }) { exercise ->
                    val dismissState = rememberDismissState()
                    var hidden by remember { mutableStateOf(false) }

                    if (!hidden) {
                        SwipeToDismiss(
                            state = dismissState,
                            background = { SwipeToDeleteBackground(dismissState) }
                        ) {
                            Card(
                                elevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                                ).value
                            ) {
                                ListItem(
                                    Modifier.clickable { addEditExercise(exercise.exerciseId) }
                                ) {
                                    Text(
                                        text = exercise.name.takeIf { it.isNotBlank() }
                                            ?: stringResource(R.string.unnamed_exercise),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }

                    if (dismissState.value != DismissValue.Default) {
                        ConfirmDeleteExerciseDialog(
                            onDismiss = { dismissState.snapTo(DismissValue.Default) },
                            exerciseName = exercise.name,
                            onConfirm = {
                                viewModel.hide(exercise, true)
                                hidden = true
                                dismissState.snapTo(DismissValue.Default)
                            },
                        )
                    }
                }
                item {
                    // Fix FAB overlap
                    Box(Modifier.height(72.dp)) {}
                }
            }
        }
    )
}

@Composable
private
fun ConfirmDeleteExerciseDialog(
    exerciseName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                stringResource(
                    R.string.confirm_delete,
                    exerciseName.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.unnamed_exercise)
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                content = { Text(stringResource(R.string.yes)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = { Text(stringResource(R.string.cancel)) }
            )
        },
        onDismissRequest = onDismiss,
    )
}
