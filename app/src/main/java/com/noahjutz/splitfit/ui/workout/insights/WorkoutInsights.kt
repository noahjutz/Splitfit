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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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
import com.noahjutz.splitfit.data.domain.Workout
import com.noahjutz.splitfit.ui.components.NothingHereYet
import com.noahjutz.splitfit.ui.components.SwipeToDeleteBackground
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
    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.tab_insights)) }) }) {
        LazyColumn {
            item {
                Column {
                    InfoTiles(workouts)

                    ProvideTextStyle(typography.h4) {
                        Box(
                            Modifier.padding(
                                bottom = 8.dp,
                                start = 16.dp,
                                end = 16.dp
                            )
                        ) {
                            Text("History")
                        }
                    }
                }
            }
            items(workouts) { workout ->
                val dismissState = rememberDismissState()

                SwipeToDismiss(
                    state = dismissState,
                    background = { SwipeToDeleteBackground(dismissState) }
                ) {
                    Card(
                        Modifier.clickable { navToWorkoutEditor(workout.workoutId) },
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

                if (dismissState.currentValue != DismissValue.Default) {
                    DeleteConfirmation(
                        workout = workout,
                        deleteWorkout = { viewModel.editor.delete(workout) },
                        resetDismissState = {
                            scope.launch {
                                dismissState.snapTo(DismissValue.Default)
                            }
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

@ExperimentalTime
@Composable
private fun InfoTiles(
    workouts: List<Workout>
) {
    Column(Modifier.padding(16.dp)) {
        Row {
            Card(Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        workouts.map { it.startTime }.currentDailyStreak.toString(),
                        style = typography.h6
                    )
                    Text("Streak", style = typography.body2)
                }
            }
            Spacer(Modifier.width(16.dp))
            Card(Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        workouts.map { it.endTime - it.startTime }.average.toString(),
                        style = typography.h6
                    )
                    Text("Average duration", style = typography.body2)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row {
            Card(Modifier.weight(1f)) {
                Box {
                    Column(Modifier.padding(16.dp)) {
                        Text(workouts.size.toString(), style = typography.h6)
                        Text("Total workouts", style = typography.body2)
                    }
                }
            }
            Spacer(Modifier.width(16.dp))
            Card(Modifier.weight(1f)) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        workouts.map { it.endTime - it.startTime }.total.toString(),
                        style = typography.h6
                    )
                    Text("Total duration", style = typography.body2)
                }
            }
        }
    }
}
