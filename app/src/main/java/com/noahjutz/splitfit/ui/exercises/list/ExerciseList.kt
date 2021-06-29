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

package com.noahjutz.splitfit.ui.exercises.list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.noahjutz.splitfit.ui.components.SearchBar
import com.noahjutz.splitfit.ui.components.SwipeToDeleteBackground
import com.noahjutz.splitfit.ui.components.TopBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ExerciseList(
    navToExerciseEditor: (Int) -> Unit,
    viewModel: ExerciseListViewModel = getViewModel(),
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBar(title = stringResource(R.string.tab_exercises))
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navToExerciseEditor(-1) },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text(stringResource(R.string.new_exercise)) },
            )
        },
        content = {
            val exercises by viewModel.exercises.collectAsState(emptyList())
            LazyColumn(Modifier.fillMaxHeight()) {
                item {
                    val searchQuery by viewModel.nameFilter.collectAsState()
                    SearchBar(
                        modifier = Modifier.padding(16.dp),
                        value = searchQuery,
                        onValueChange = viewModel::setNameFilter
                    )
                }

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
                                    Modifier.clickable { navToExerciseEditor(exercise.exerciseId) }
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

                    if (dismissState.targetValue != DismissValue.Default) {
                        ConfirmDeleteExerciseDialog(
                            onDismiss = { scope.launch { dismissState.reset() } },
                            exerciseName = exercise.name,
                            onConfirm = {
                                viewModel.hide(exercise, true)
                                hidden = true
                                scope.launch { dismissState.reset() }
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
