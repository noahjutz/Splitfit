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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.Set
import com.noahjutz.splitfit.util.RegexPatterns

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview
@Composable
fun SetGroupCardPreview() {
    MaterialTheme(colors = darkColors()) {
        SetGroupCard(
            name = "Exercise",
            onMoveDown = {},
            onMoveUp = {},
            sets = listOf(Set(1, 2.0), Set(12, 3.0)),
            logReps = true,
            logWeight = true,
            logTime = true,
            logDistance = true,
            showCheckbox = true,
        )
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
        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            SetGroupTitle(
                Modifier.padding(horizontal = 16.dp),
                name = name,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
            )
            Spacer(Modifier.preferredHeight(8.dp))
            SetTable(
                Modifier.padding(horizontal = 16.dp),
                sets = sets,
                logReps = logReps,
                logWeight = logWeight,
                logTime = logTime,
                logDistance = logDistance,
                showCheckbox = showCheckbox,
            )
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
    Row(modifier.preferredHeight(48.dp), verticalAlignment = Alignment.CenterVertically) {
        ProvideTextStyle(typography.h6) {
            Text(name.takeIf { it.isNotBlank() } ?: stringResource(R.string.unnamed_routine))
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
    Surface(
        modifier,
        shape = RoundedCornerShape(4.dp),
        elevation = 0.dp,
        border = BorderStroke(1.dp, colors.onSurface.copy(alpha = 0.12f))
    ) {
        Column {
            SetTableHeader(
                logReps = logReps,
                logWeight = logWeight,
                logTime = logTime,
                logDistance = logDistance,
                adjustForCheckbox = showCheckbox,
            )
            Divider()

            for (set in sets) {
                // TODO blank strings if null instead of "null"
                // TODO callback for on[Value]Change
                // TODO conditionally declare set[Value] to null or pass log[Value]
                val (reps, setReps) = remember(set.reps) { mutableStateOf(set.reps) }
                val (weight, setWeight) = remember(set.weight) { mutableStateOf(set.weight) }
                val (duration, setDuration) = remember(set.time) { mutableStateOf(set.time) }
                val (distance, setDistance) = remember(set.distance) { mutableStateOf(set.distance) }
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

            TableRow(Modifier.padding(horizontal = 8.dp)) {
                TextButton(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.preferredWidth(8.dp))
                    Text("Add Set")
                }
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
    adjustForCheckbox: Boolean,
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
        if (adjustForCheckbox) Spacer(Modifier.preferredWidth(48.dp))
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun ColumnScope.TableSetRow(
    modifier: Modifier = Modifier,
    reps: Int? = null,
    onRepsChange: ((Int?) -> Unit)? = null,
    weight: Double? = null,
    onWeightChange: ((Double?) -> Unit)? = null,
    duration: Int? = null,
    onDurationChange: ((Int?) -> Unit)? = null,
    distance: Double? = null,
    onDistanceChange: ((Double?) -> Unit)? = null,
    showCheckbox: Boolean,
) {
    DismissibleTableRow(modifier.padding(horizontal = 16.dp), rememberDismissState()) {
        if (onRepsChange != null) TableCell(Modifier.weight(1f)) {
            IntegerTextField(value = reps, onValueChange = onRepsChange)
        }
        if (onWeightChange != null) TableCell(Modifier.weight(1f)) {
            DoubleTextField(value = weight, onValueChange = onWeightChange)
        }
        if (onDurationChange != null) TableCell(Modifier.weight(1f)) {
            DurationTextField(value = duration, onValueChange = onDurationChange)
        }
        if (onDistanceChange != null) TableCell(Modifier.weight(1f)) {
            DoubleTextField(value = distance, onValueChange = onDistanceChange)
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
    value: Int?,
    onValueChange: (Int?) -> Unit,
) {
    TableCellTextField(
        value = value?.toString() ?: "",
        onValueChange = { if (it.matches(RegexPatterns.integer)) onValueChange(it.toIntOrNull()) },
        hint = "0",
    )
}

@Composable
private fun DoubleTextField(
    value: Double?,
    onValueChange: (Double?) -> Unit,
) {
    TableCellTextField(
        value = value?.toString() ?: "",
        onValueChange = { if (it.matches(RegexPatterns.float)) onValueChange(it.toDoubleOrNull()) },
        hint = "0.0",
    )
}

@Composable
private fun DurationTextField(
    value: Int?,
    onValueChange: (Int?) -> Unit,
) {
    TableCellTextField(
        value = value?.toString() ?: "",
        onValueChange = { if (it.matches(RegexPatterns.duration)) onValueChange(it.toIntOrNull()) },
        hint = "00:00",
        visualTransformation = durationVisualTransformation,
    )
}