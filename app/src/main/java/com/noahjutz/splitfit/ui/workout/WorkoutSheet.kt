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

import androidx.compose.animation.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun WorkoutBottomSheet(
    bottomSheetState: BottomSheetState,
    stopWorkout: () -> Unit,
) {
    BottomSheetPeek(bottomSheetState)
    WorkoutDetails(stopWorkout)
}


@ExperimentalMaterialApi
@Composable
fun BottomSheetPeek(
    bottomSheet: BottomSheetState,
) {
    val toggleBottomSheet = {
        if (bottomSheet.isCollapsed) bottomSheet.expand() else bottomSheet.collapse()
    }
    val backgroundColor =
        animate(if (bottomSheet.isCollapsed) MaterialTheme.colors.surface else MaterialTheme.colors.primarySurface)
    TopAppBar(
        modifier = Modifier.clickable { toggleBottomSheet() },
        backgroundColor = backgroundColor,
        title = { Text("Workout") },
        actions = {
            val rotation = animate(if (bottomSheet.isCollapsed) 0f else 180f)
            IconButton(
                onClick = { toggleBottomSheet() },
                content = {
                    Icon(
                        Icons.Default.ExpandLess,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            )
        }
    )
}

@Composable
fun WorkoutDetails(stopWorkout: () -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize(),
    ) {
        item {
            Button(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                onClick = { stopWorkout() }
            ) {
                Text("Stop workout")
            }
        }
        repeat(100) { index ->
            item {
                val modifier =
                    if (index % 2 == 0) Modifier.background(MaterialTheme.colors.onSurface.copy(
                        alpha = 0.12f)) else Modifier
                Box(modifier.fillMaxWidth().preferredHeight(150.dp))
            }
        }
    }
}
