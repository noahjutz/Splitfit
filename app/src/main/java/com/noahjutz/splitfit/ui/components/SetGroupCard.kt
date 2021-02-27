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
import androidx.compose.ui.platform.LocalFocusManager
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

        var sets by remember {
            mutableStateOf(listOf(Set(1, 2.0),
                Set(distance = 3.0),
                Set(),
                Set()))
        }

        val onMoveDown = {
            logWithSnackbar("onMoveDown")
        }
        val onMoveUp = {
            logWithSnackbar("onMoveUp")
        }
        val onAddSet = {
            logWithSnackbar("onAddSet")
            sets = sets + sets.last().copy(complete = false)
        }
        val onDistanceChange = { i: Int, d: String ->
            logWithSnackbar("i: $i, d: $d")
            sets = sets.toMutableList().apply {
                this[i] = this[i].copy(distance = d.toDoubleOrNull())
            }
        }
        val onTimeChange = { i: Int, t: String ->
            logWithSnackbar("i: $i, t: $t")
            sets = sets.toMutableList().apply {
                this[i] = this[i].copy(time = t.toIntOrNull())
            }
        }
        val onRepsChange = { i: Int, r: String ->
            logWithSnackbar("i: $i, r: $r")
            sets = sets.toMutableList().apply {
                this[i] = this[i].copy(reps = r.toIntOrNull())
            }
        }
        val onWeightChange = { i: Int, w: String ->
            logWithSnackbar("i: $i, w: $w")
            sets = sets.toMutableList().apply {
                this[i] = this[i].copy(weight = w.toDoubleOrNull())
            }
        }
        val onCheckboxChange = { i: Int, b: Boolean ->
            logWithSnackbar("i: $i, b: $b")
            sets = sets.toMutableList().apply {
                this[i] = this[i].copy(complete = b)
            }
        }
        val onDeleteSet = { i: Int ->
            logWithSnackbar("i: $i")
            sets = sets.toMutableList().apply { removeAt(i) }
        }

        Scaffold(scaffoldState = scaffoldState) {
            SetGroupCard(
                name = "Weighted Walking Lunges bla bla bla bla bla blab bla",
                onMoveDown = onMoveDown,
                onMoveUp = onMoveUp,
                onAddSet = onAddSet,
                sets = sets,
                logReps = true,
                logWeight = true,
                logTime = true,
                logDistance = true,
                showCheckbox = true,
                onDistanceChange = onDistanceChange,
                onTimeChange = onTimeChange,
                onRepsChange = onRepsChange,
                onWeightChange = onWeightChange,
                onCheckboxChange = onCheckboxChange,
                onDeleteSet = onDeleteSet,
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
    onDeleteSet: (Int) -> Unit,
    logReps: Boolean,
    onRepsChange: (Int, String) -> Unit = { _, _ -> },
    logWeight: Boolean,
    onWeightChange: (Int, String) -> Unit = { _, _ -> },
    logTime: Boolean,
    onTimeChange: (Int, String) -> Unit = { _, _ -> },
    logDistance: Boolean,
    onDistanceChange: (Int, String) -> Unit = { _, _ -> },
    showCheckbox: Boolean,
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
                onCheckboxChange = onCheckboxChange,
                onAddSet = onAddSet,
                onDeleteSet = onDeleteSet,
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
            modifier
                .height(70.dp)
                .padding(start = 16.dp, end = 8.dp),
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
    onCheckboxChange: (Int, Boolean) -> Unit,
    onAddSet: () -> Unit,
    onDeleteSet: (Int) -> Unit,
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
            var reps by remember { mutableStateOf(set.reps.toStringOrBlank()) }
            var weight by remember { mutableStateOf(set.weight.formatSimple()) }
            var time by remember { mutableStateOf(set.time.toStringOrBlank()) }
            var distance by remember { mutableStateOf(set.distance.formatSimple()) }

            TableSetRow(
                logReps = logReps,
                reps = reps,
                onRepsChange = {
                    reps = it
                    onRepsChange(i, it)
                },
                logWeight = logWeight,
                weight = weight,
                onWeightChange = {
                    weight = it
                    onWeightChange(i, it)
                },
                logDuration = logTime,
                duration = time,
                onDurationChange = {
                    time = it
                    onTimeChange(i, it)
                },
                logDistance = logDistance,
                distance = distance,
                onDistanceChange = {
                    distance = it
                    onDistanceChange(i, it)
                },
                showCheckbox = showCheckbox,
                checkboxChecked = set.complete,
                onCheckboxChange = { onCheckboxChange(i, it) },
                onDeleteSet = { onDeleteSet(i) }
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
    onDeleteSet: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val dismissState = rememberDismissState()

    val isDismissed = dismissState.targetValue != DismissValue.Default

    if (isDismissed) ConfirmDeleteSetDialog(
        onDismiss = { scope.launch { dismissState.reset() } },
        onConfirm = onDeleteSet
    )

    val focusManager = LocalFocusManager.current
    LaunchedEffect(isDismissed) {
        if (isDismissed) focusManager.clearFocus()
    }

    DismissibleTableRow(
        modifier.padding(start = 16.dp, end = if (showCheckbox) 8.dp else 16.dp),
        state = dismissState,
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

@Composable
private fun ConfirmDeleteSetDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text("Do you want to delete this set?") },
        confirmButton = { Button(onClick = onConfirm) { Text(stringResource(R.string.yes)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
    )
}