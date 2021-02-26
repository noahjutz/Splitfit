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

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.Set
import com.noahjutz.splitfit.util.RegexPatterns
import com.noahjutz.splitfit.util.formatSimple
import com.noahjutz.splitfit.util.toStringOrBlank
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview
@Composable
fun SetGroupCardPreview() {
    MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        fun logWithSnackbar(message: String) {
            Log.d("SetGroupCard", message)
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
        }

        val onMoveDown = { logWithSnackbar("onMoveDown") }
        val onMoveUp = { logWithSnackbar("onMoveUp") }
        val onAddSet = { logWithSnackbar("onAddSet") }
        val onDistanceChange = { i: Int, d: String -> logWithSnackbar("i: $i, d: $d") }
        val onTimeChange = { i: Int, t: String -> logWithSnackbar("i: $i, t: $t") }
        val onRepsChange = { i: Int, r: String -> logWithSnackbar("i: $i, r: $r") }
        val onWeightChange = { i: Int, w: String -> logWithSnackbar("i: $i, w: $w") }
        val onCheckboxChange = { i: Int, b: Boolean -> logWithSnackbar("i: $i, b: $b") }

        Scaffold(scaffoldState = scaffoldState) {
            SetGroupCard(
                name = "Weighted Walking Lunges bla bla bla bla bla blab bla",
                onMoveDown = onMoveDown,
                onMoveUp = onMoveUp,
                onAddSet = onAddSet,
                sets = listOf(Set(1, 2.0), Set(distance = 3.0), Set(), Set()),
                logReps = true,
                logWeight = true,
                logTime = true,
                logDistance = true,
                showCheckbox = true,
                onDistanceChange = onDistanceChange,
                onTimeChange = onTimeChange,
                onRepsChange = onRepsChange,
                onWeightChange = onWeightChange,
                checkboxChecked = true,
                onCheckboxChange = onCheckboxChange,
            )
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
    onAddSet: () -> Unit,
    logReps: Boolean,
    onRepsChange: (Int, String) -> Unit = { _, _ -> },
    logWeight: Boolean,
    onWeightChange: (Int, String) -> Unit = { _, _ -> },
    logTime: Boolean,
    onTimeChange: (Int, String) -> Unit = { _, _ -> },
    logDistance: Boolean,
    onDistanceChange: (Int, String) -> Unit = { _, _ -> },
    showCheckbox: Boolean,
    checkboxChecked: Boolean = false,
    onCheckboxChange: (Int, Boolean) -> Unit = { _, _ -> },
) {
    Card(elevation = 0.dp) {
        Column(Modifier.fillMaxWidth()) {
            SetGroupTitle(
                name = name,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
            )
            SetTable(
                Modifier.padding(horizontal = 8.dp),
                sets = sets,
                logReps = logReps,
                onRepsChange = onRepsChange,
                logWeight = logWeight,
                onWeightChange = onWeightChange,
                logTime = logTime,
                onTimeChange = onTimeChange,
                logDistance = logDistance,
                onDistanceChange = onDistanceChange,
                showCheckbox = showCheckbox,
                checkboxChecked = checkboxChecked,
                onCheckboxChange = onCheckboxChange,
                onAddSet = onAddSet,
            )
            Spacer(Modifier.height(8.dp))
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
            modifier.height(70.dp).padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(typography.h5) {
                Text(
                    name.takeIf { it.isNotBlank() } ?: stringResource(R.string.unnamed_exercise),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            }
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
    onRepsChange: (Int, String) -> Unit,
    logWeight: Boolean,
    onWeightChange: (Int, String) -> Unit,
    logTime: Boolean,
    onTimeChange: (Int, String) -> Unit,
    logDistance: Boolean,
    onDistanceChange: (Int, String) -> Unit,
    showCheckbox: Boolean,
    checkboxChecked: Boolean,
    onCheckboxChange: (Int, Boolean) -> Unit,
    onAddSet: () -> Unit,
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

        sets.forEachIndexed { i, set ->
            TableSetRow(
                logReps = logReps,
                reps = set.reps.toStringOrBlank(),
                onRepsChange = { onRepsChange(i, it) },
                logWeight = logWeight,
                weight = set.weight.formatSimple(),
                onWeightChange = { onWeightChange(i, it) },
                logDuration = logTime,
                duration = set.time.toStringOrBlank(),
                onDurationChange = { onTimeChange(i, it) },
                logDistance = logDistance,
                distance = set.distance.formatSimple(),
                onDistanceChange = { onDistanceChange(i, it) },
                showCheckbox = showCheckbox,
                checkboxChecked = checkboxChecked,
                onCheckboxChange = { onCheckboxChange(i, it) },
            )
            Divider()
        }

        TableRow {
            TextButton(
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                onClick = onAddSet,
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.height(8.dp))
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
        if (showCheckbox) Spacer(Modifier.width(40.dp))
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun ColumnScope.TableSetRow(
    modifier: Modifier = Modifier,
    logReps: Boolean,
    reps: String = "",
    onRepsChange: ((String) -> Unit) = {},
    logWeight: Boolean,
    weight: String = "",
    onWeightChange: ((String) -> Unit) = {},
    logDuration: Boolean,
    duration: String = "",
    onDurationChange: ((String) -> Unit) = {},
    logDistance: Boolean,
    distance: String = "",
    onDistanceChange: ((String) -> Unit) = {},
    showCheckbox: Boolean,
    checkboxChecked: Boolean,
    onCheckboxChange: (Boolean) -> Unit,
) {
    DismissibleTableRow(
        modifier.padding(start = 16.dp, end = if (showCheckbox) 8.dp else 16.dp),
        rememberDismissState() // TODO
    ) {
        if (logReps) TableCell(Modifier.weight(1f)) {
            IntegerTextField(value = reps, onValueChange = onRepsChange)
        }
        if (logWeight) TableCell(Modifier.weight(1f)) {
            FloatTextField(value = weight, onValueChange = onWeightChange)
        }
        if (logDuration) TableCell(Modifier.weight(1f)) {
            DurationTextField(value = duration, onValueChange = onDurationChange)
        }
        if (logDistance) TableCell(Modifier.weight(1f)) {
            FloatTextField(value = distance, onValueChange = onDistanceChange)
        }
        if (showCheckbox) TableCell {
            Checkbox(
                modifier = Modifier.size(48.dp),
                checked = checkboxChecked,
                onCheckedChange = onCheckboxChange,
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