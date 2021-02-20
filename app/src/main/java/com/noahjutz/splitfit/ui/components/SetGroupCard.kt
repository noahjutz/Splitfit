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

package com.noahjutz.splitfit.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.Set
import com.noahjutz.splitfit.util.RegexPatterns
import com.noahjutz.splitfit.util.toStringOrBlank

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview
@Composable
fun SetGroupCardPreview() {
    MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
        LazyColumn {
            item {
                SetGroupCard(
                    name = "Push-up",
                    onMoveDown = {},
                    onMoveUp = {},
                    sets = listOf(Set(1, 2.0), Set(12, 3.0)),
                    logReps = true,
                    logWeight = true,
                    logTime = false,
                    logDistance = false,
                    showCheckbox = true,
                )
                SetGroupCard(
                    name = "Jump Rope",
                    onMoveDown = {},
                    onMoveUp = {},
                    sets = listOf(Set(time = 33)),
                    logReps = false,
                    logWeight = false,
                    logTime = true,
                    logDistance = false,
                    showCheckbox = true,
                )
                SetGroupCard(
                    name = "Weighted Walking Lunges",
                    onMoveDown = {},
                    onMoveUp = {},
                    sets = listOf(Set(1, 2.0), Set(distance = 3.0), Set(), Set()),
                    logReps = true,
                    logWeight = true,
                    logTime = true,
                    logDistance = true,
                    showCheckbox = true,
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SetGroupCard(
    name: String,
    sets: List<Set>,
    onMoveDown: () -> Unit,
    onMoveUp: () -> Unit,
    logReps: Boolean,
    logWeight: Boolean,
    logTime: Boolean,
    logDistance: Boolean,
    showCheckbox: Boolean,
) {
    Card(elevation = 0.dp) {
        Column(Modifier.fillMaxWidth()) {
            SetGroupTitle(
                Modifier.padding(start = 16.dp, end = 8.dp),
                name = name,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
            )
            SetTable(
                Modifier.padding(horizontal = 8.dp),
                sets = sets,
                logReps = logReps,
                logWeight = logWeight,
                logTime = logTime,
                logDistance = logDistance,
                showCheckbox = showCheckbox,
            )
            Spacer(Modifier.preferredHeight(8.dp))
        }
    }
}

@Composable
private fun SetGroupTitle(
    modifier: Modifier = Modifier,
    name: String,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
) {
    Box(Modifier.clickable {}) {
        Row(
            modifier.preferredHeight(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(typography.h5) {
                Text(name.takeIf { it.isNotBlank() } ?: stringResource(R.string.unnamed_exercise))
            }
            Spacer(Modifier.weight(1f))
            // Temporary replacement for drag & drop.
            // See https://stackoverflow.com/questions/64913067
            Box {
                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, "More") }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(onClick = onMoveUp) {
                        Text("Move up")
                    }
                    DropdownMenuItem(onClick = onMoveDown) {
                        Text("Move down")
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun SetTable(
    modifier: Modifier = Modifier,
    sets: List<Set>,
    logReps: Boolean,
    logWeight: Boolean,
    logTime: Boolean,
    logDistance: Boolean,
    showCheckbox: Boolean,
) {
    Table(modifier) {
        SetTableHeader(
            logReps = logReps,
            logWeight = logWeight,
            logTime = logTime,
            logDistance = logDistance,
            showCheckbox = showCheckbox,
        )
        Divider()

        for (set in sets) {
            // TODO callback for on[Value]Change
            // TODO conditionally declare set[Value] to null or pass log[Value]
            val (reps, setReps) = remember(set.reps) { mutableStateOf(set.reps.toStringOrBlank()) }
            val (weight, setWeight) = remember(set.weight) { mutableStateOf(set.weight.toStringOrBlank()) }
            val (duration, setDuration) = remember(set.time) { mutableStateOf(set.time.toStringOrBlank()) }
            val (distance, setDistance) = remember(set.distance) { mutableStateOf(set.distance.toStringOrBlank()) }
            TableSetRow(
                reps = reps,
                onRepsChange = setReps,
                weight = weight,
                onWeightChange = setWeight,
                duration = duration,
                onDurationChange = setDuration,
                distance = distance,
                onDistanceChange = setDistance,
                showCheckbox = showCheckbox,
            )
            Divider()
        }

        TableRow {
            TextButton(
                modifier = Modifier.preferredHeight(52.dp).fillMaxWidth(),
                shape = RectangleShape,
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.preferredWidth(8.dp))
                Text("Add Set")
            }
        }
    }
}

@Composable
private fun ColumnScope.SetTableHeader(
    logReps: Boolean,
    logWeight: Boolean,
    logTime: Boolean,
    logDistance: Boolean,
    showCheckbox: Boolean,
) {
    TableHeaderRow(Modifier.padding(horizontal = 16.dp)) {
        if (logReps) TableCell(Modifier.weight(1f)) {
            Text(stringResource(R.string.reps))
        }
        if (logWeight) TableCell(Modifier.weight(1f)) {
            Text(stringResource(R.string.weight))
        }
        if (logTime) TableCell(Modifier.weight(1f)) {
            Text(stringResource(R.string.time))
        }
        if (logDistance) TableCell(Modifier.weight(1f)) {
            Text(stringResource(R.string.distance))
        }
        if (showCheckbox) Spacer(Modifier.preferredWidth(40.dp))
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun ColumnScope.TableSetRow(
    modifier: Modifier = Modifier,
    reps: String = "",
    onRepsChange: ((String) -> Unit)? = null,
    weight: String = "",
    onWeightChange: ((String) -> Unit)? = null,
    duration: String = "",
    onDurationChange: ((String) -> Unit)? = null,
    distance: String = "",
    onDistanceChange: ((String) -> Unit)? = null,
    showCheckbox: Boolean,
) {
    DismissibleTableRow(
        modifier.padding(start = 16.dp, end = if (showCheckbox) 8.dp else 16.dp),
        rememberDismissState() // TODO
    ) {
        if (onRepsChange != null) TableCell(Modifier.weight(1f)) {
            IntegerTextField(value = reps, onValueChange = onRepsChange)
        }
        if (onWeightChange != null) TableCell(Modifier.weight(1f)) {
            FloatTextField(value = weight, onValueChange = onWeightChange)
        }
        if (onDurationChange != null) TableCell(Modifier.weight(1f)) {
            DurationTextField(value = duration, onValueChange = onDurationChange)
        }
        if (onDistanceChange != null) TableCell(Modifier.weight(1f)) {
            FloatTextField(value = distance, onValueChange = onDistanceChange)
        }
        if (showCheckbox) TableCell {
            Checkbox(
                modifier = Modifier.preferredSize(48.dp),
                checked = false,
                onCheckedChange = { /*TODO*/ }
            )
        }
    }
}

@Composable
private fun IntegerTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TableCellTextField(
        value = value,
        onValueChange = { if (it.matches(RegexPatterns.integer)) onValueChange(it) },
        hint = "0",
    )
}

@Composable
private fun FloatTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TableCellTextField(
        value = value,
        onValueChange = { if (it.matches(RegexPatterns.float)) onValueChange(it) },
        hint = "0.0",
    )
}

@Composable
private fun DurationTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TableCellTextField(
        value = value,
        onValueChange = { if (it.matches(RegexPatterns.duration)) onValueChange(it) },
        hint = "00:00",
        visualTransformation = durationVisualTransformation,
    )
}