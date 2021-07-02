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

package com.noahjutz.splitfit.ui.workout.insights

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.ColorTheme
import com.noahjutz.splitfit.data.domain.Workout
import com.noahjutz.splitfit.ui.LocalColorTheme
import com.noahjutz.splitfit.ui.components.NormalDialog
import com.noahjutz.splitfit.ui.components.NothingHereYet
import com.noahjutz.splitfit.ui.components.SwipeToDeleteBackground
import com.noahjutz.splitfit.ui.components.TopBar
import com.noahjutz.splitfit.util.average
import com.noahjutz.splitfit.util.currentDailyStreak
import com.noahjutz.splitfit.util.minus
import com.noahjutz.splitfit.util.total
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalMaterialApi
@ExperimentalTime
@Composable
fun WorkoutInsights(
    viewModel: WorkoutInsightsViewModel = getViewModel(),
    navToWorkoutEditor: (Int) -> Unit,
) {
    val workouts by viewModel.presenter.workouts.collectAsState(initial = emptyList())

    if (workouts.isEmpty()) NothingHereYet("Insights will be available when you finish your first workout.")
    else WorkoutInsightsContent(viewModel, navToWorkoutEditor)
}

@ExperimentalTime
@ExperimentalMaterialApi
@Composable
fun WorkoutInsightsContent(
    viewModel: WorkoutInsightsViewModel = getViewModel(),
    navToWorkoutEditor: (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val workouts by viewModel.presenter.workouts.collectAsState(emptyList())
    Scaffold(topBar = { TopBar(title = stringResource(R.string.tab_insights)) }) {
        LazyColumn {
            item {
                InfoTiles(workouts)
                Text(
                    "History",
                    Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                    style = typography.h5
                )
            }
            items(workouts) { workout ->
                val dismissState = rememberDismissState()

                SwipeToDismiss(
                    state = dismissState,
                    background = { SwipeToDeleteBackground(dismissState) }
                ) {
                    Card(
                        onClick = { navToWorkoutEditor(workout.workoutId) },
                        elevation = animateDpAsState(
                            if (dismissState.dismissDirection != null) 4.dp else 0.dp
                        ).value,
                    ) {
                        ListItem(
                            text = {
                                Text(
                                    text = workout.name.takeIf { it.isNotBlank() }
                                        ?: stringResource(R.string.unnamed_workout),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            },
                            secondaryText = {
                                val day = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(workout.startTime)
                                Text(day)
                            }
                        )
                    }
                }

                if (dismissState.targetValue != DismissValue.Default) {
                    DeleteConfirmation(
                        workout = workout,
                        deleteWorkout = { viewModel.editor.delete(workout) },
                        resetDismissState = {
                            scope.launch { dismissState.reset() }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmation(
    workout: Workout,
    deleteWorkout: () -> Unit,
    resetDismissState: () -> Unit,
) {
    NormalDialog(
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

@ExperimentalTime
@Composable
private fun InfoTiles(
    workouts: List<Workout>
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = if (LocalColorTheme.current == ColorTheme.White) 2.dp else 0.dp,
        border = BorderStroke(2.dp, colors.onSurface.copy(alpha = 0.12f))
            .takeIf { LocalColorTheme.current == ColorTheme.Black }
    ) {
        Column {
            Row {
                InfoTile(
                    text = workouts.map { it.startTime }.currentDailyStreak.toString(),
                    secondaryText = "Streak"
                )
                InfoTile(
                    text = workouts.map { it.endTime - it.startTime }.average.toString(),
                    secondaryText = "Average Duration"
                )
            }
            Row {
                InfoTile(
                    text = workouts.size.toString(),
                    secondaryText = "Total Workouts"
                )
                InfoTile(
                    text = workouts.map { it.endTime - it.startTime }.total.toString(),
                    secondaryText = "Total duration"
                )
            }
        }
    }
}

@Composable
private fun RowScope.InfoTile(
    text: String,
    secondaryText: String,
) {
    Column(
        Modifier
            .weight(1f)
            .padding(16.dp)
    ) {
        Text(text, style = typography.h6)
        Text(secondaryText, style = typography.body2)
    }
}
