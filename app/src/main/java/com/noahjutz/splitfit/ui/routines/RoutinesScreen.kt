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

package com.noahjutz.splitfit.ui.routines

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.util.SwipeToDeleteBackground
import org.koin.androidx.compose.getViewModel
import org.koin.core.context.GlobalContext.get

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun RoutinesScreen(
    addEditRoutine: (Int) -> Unit,
    viewModel: RoutinesViewModel = getViewModel(),
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addEditRoutine(viewModel.addRoutine().toInt()) },
                content = { Icon(Icons.Default.Add) }
            )
        },
    ) {

        val routines by viewModel.routines.collectAsState(emptyList())

        LazyColumn(Modifier.fillMaxHeight()) {
            items(items = routines) { routine ->
                val dismissState = rememberDismissState()

                SwipeToDismiss(
                    state = dismissState,
                    background = { SwipeToDeleteBackground(dismissState) }
                ) {
                    Card(
                        elevation = animateAsState(
                            if (dismissState.dismissDirection != null) 4.dp else 0.dp
                        ).value
                    ) {
                        ListItem(Modifier.clickable { addEditRoutine(routine.routineId) }) {
                            Text(
                                text = routine.name.takeIf { it.isNotBlank() }
                                    ?: stringResource(R.string.unnamed_routine),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }

                if (dismissState.value != DismissValue.Default) {
                    AlertDialog(
                        title = {
                            Text(
                                stringResource(
                                    R.string.confirm_delete,
                                    routine.name.takeIf { it.isNotBlank() }
                                        ?: stringResource(R.string.unnamed_routine)
                                )
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.deleteRoutine(routine.routineId)
                                    dismissState.snapTo(DismissValue.Default)
                                },
                                content = { Text(stringResource(R.string.yes)) }
                            )
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    dismissState.snapTo(DismissValue.Default)
                                },
                                content = { Text(stringResource(R.string.cancel)) }
                            )
                        },
                        onDismissRequest = {
                            dismissState.snapTo(DismissValue.Default)
                        }
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
