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

package com.noahjutz.splitfit.ui.workout

import androidx.compose.animation.core.animateAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.Workout
import com.noahjutz.splitfit.ui.components.SwipeToDeleteBackground
import com.noahjutz.splitfit.util.DatastoreKeys
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun WorkoutsScreen(
    viewModel: WorkoutsViewModel = getViewModel(),
    navToCreateWorkoutScreen: (Int) -> Unit,
    preferences: DataStore<Preferences> = get(),
) {
    val workout by viewModel.presenter.workouts.collectAsState(emptyList())
    val preferencesData by preferences.data.collectAsState(null)
    LazyColumn {
        items(workout) { workout ->
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
                    ListItem {
                        Text(
                            text = workout.name.takeIf { it.isNotBlank() }
                                ?: stringResource(R.string.unnamed_workout),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            if (dismissState.value != DismissValue.Default) {
                if (preferencesData?.get(DatastoreKeys.currentWorkout) == workout.workoutId) {
                    CannotDeleteAlert(
                        resetDismissState = { dismissState.snapTo(DismissValue.Default) }
                    )
                } else {
                    DeleteConfirmation(
                        workout = workout,
                        deleteWorkout = { viewModel.editor.delete(workout) },
                        resetDismissState = { dismissState.snapTo(DismissValue.Default) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CannotDeleteAlert(
    resetDismissState: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = resetDismissState,
        title = { Text("Cannot delete workout") },
        text = { Text("This workout is currently in progress. Please finish it before deleting it.") },
        confirmButton = {},
        dismissButton = { TextButton(onClick = resetDismissState) { Text("Close") } },
    )
}

@Composable
private fun DeleteConfirmation(
    workout: Workout,
    deleteWorkout: () -> Unit,
    resetDismissState: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                stringResource(
                    R.string.confirm_delete,
                    workout.name.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.unnamed_workout)
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    deleteWorkout()
                    resetDismissState()
                },
                content = { Text(stringResource(R.string.yes)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = resetDismissState,
                content = { Text(stringResource(R.string.cancel)) }
            )
        },
        onDismissRequest = resetDismissState
    )
}