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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.ui.components.NormalDialog
import com.noahjutz.splitfit.ui.components.SwipeToDeleteBackground
import com.noahjutz.splitfit.ui.components.TopBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun RoutineList(
    addEditRoutine: (Int) -> Unit,
    viewModel: RoutineListViewModel = getViewModel(),
) {
    Scaffold(
        topBar = { TopBar(title = stringResource(R.string.tab_routines)) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { addEditRoutine(viewModel.addRoutine().toInt()) },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("New Routine") },
            )
        },
    ) {
        val scope = rememberCoroutineScope()

        val routines by viewModel.routines.collectAsState(emptyList())

        LazyColumn(Modifier.fillMaxHeight()) {
            items(items = routines) { routine ->
                val dismissState = rememberDismissState()

                SwipeToDismiss(
                    state = dismissState,
                    background = { SwipeToDeleteBackground(dismissState) }
                ) {
                    Card(
                        elevation = animateDpAsState(
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

                if (dismissState.targetValue != DismissValue.Default) {
                    NormalDialog(
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
                                    scope.launch { dismissState.reset() }
                                },
                                content = { Text(stringResource(R.string.yes)) }
                            )
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { scope.launch { dismissState.reset() } },
                                content = { Text(stringResource(R.string.cancel)) }
                            )
                        },
                        onDismissRequest = { scope.launch { dismissState.reset() } }
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
