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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.Set
import java.util.*
import kotlin.math.floor

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
            logTime = false,
            logDistance = false,
            showCheckbox = false,
        )
    }
}

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
            Spacer(Modifier.preferredHeight(8.dp))
            SetGroupCardButtons(
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown,
            )
        }
    }
}

@Composable
private fun SetGroupTitle(
    modifier: Modifier = Modifier,
    name: String,
) {
    Row(modifier.preferredHeight(48.dp), verticalAlignment = Alignment.CenterVertically) {
        ProvideTextStyle(typography.h6) {
            Text(name.takeIf { it.isNotBlank() } ?: stringResource(R.string.unnamed_routine))
        }
    }
}

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
            sets.forEachIndexed { i, set ->
                SetTableSetRow(
                    set = set,
                    logReps = logReps,
                    logWeight = logWeight,
                    logTime = logTime,
                    logDistance = logDistance,
                    showCheckbox = showCheckbox,
                )
                if (i < sets.lastIndex) Divider()
            }
        }
    }
}

@Composable
private fun ColumnScope.SetTableHeader(
    modifier: Modifier = Modifier,
    logReps: Boolean,
    logWeight: Boolean,
    logTime: Boolean,
    logDistance: Boolean,
    adjustForCheckbox: Boolean,
) {
    SetTableRow(modifier.preferredHeight(56.dp)) {
        if (logReps) SetTableHeaderCell(stringResource(R.string.reps))
        if (logWeight) SetTableHeaderCell(stringResource(R.string.weight))
        if (logTime) SetTableHeaderCell(stringResource(R.string.time))
        if (logDistance) SetTableHeaderCell(stringResource(R.string.distance))
        if (adjustForCheckbox) Spacer(Modifier.preferredWidth(48.dp))
    }
}

@Composable
private fun RowScope.SetTableHeaderCell(
    text: String,
    modifier: Modifier = Modifier,
) {
    SetTableCell(modifier.weight(1f)) {
        Text(
            text = text.capitalize(Locale.getDefault()),
            maxLines = 1,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RowScope.SetTableCell(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier) {
        content()
    }
}

@Composable
private fun ColumnScope.SetTableRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier.preferredHeight(52.dp)
            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@ExperimentalFoundationApi
@Composable
private fun ColumnScope.SetTableSetRow(
    modifier: Modifier = Modifier,
    set: Set,
    logReps: Boolean,
    logWeight: Boolean,
    logTime: Boolean,
    logDistance: Boolean,
    showCheckbox: Boolean,
) {
    SetTableRow(modifier) {
        var value = ""
        if (logReps) SetTableCell(Modifier.weight(1f)) {
            SetTextField(value = value, onValueChange = { value = it })
        }
        if (logWeight) SetTableCell(Modifier.weight(1f)) {
            SetTextField(value = "", onValueChange = {})
        }
        if (logTime) SetTableCell(Modifier.weight(1f)) {
            SetTextField(value = "", onValueChange = {})
        }
        if (logDistance) SetTableCell(Modifier.weight(1f)) {
            SetTextField(value = "", onValueChange = {})
        }
    }
}

@Composable
private fun SetTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    regexPattern: Regex = Regex(""),
) {
    var tfValue by remember { mutableStateOf(TextFieldValue(value)) }
    DisposableEffect(value) {
        val v = when {
            value.isEmpty() -> value
            value.toDouble() == floor(value.toDouble()) -> value.toDouble().toInt().toString()
            else -> value
        }
        if (tfValue.text != value) tfValue = TextFieldValue(v, TextRange(value.length))
        onDispose {}
    }

    // onValueChange is called after onFocusChanged, overriding the selection in onFocusChanged.
    // Fix: Lock onValueChange when calling onFocusChanged
    var valueChangeLock = false
    Box(Modifier.preferredHeight(48.dp), contentAlignment = Alignment.CenterStart) {
        BasicTextField(
            value = tfValue,
            onValueChange = {
                if (valueChangeLock) {
                    valueChangeLock = false
                    return@BasicTextField
                }
                if (it.text.matches(regexPattern)) {
                    tfValue = TextFieldValue(it.text, TextRange(it.text.length))
                    onValueChange(it.text)
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        valueChangeLock = true
                        tfValue = TextFieldValue(value, TextRange(0, value.length))
                    }
                },
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(
                color = colors.onSurface
            ),
            cursorColor = colors.onSurface,
            maxLines = 1,
            decorationBox = { innerTextField ->
                if (value.isBlank()) Text("Hint", modifier = Modifier.alpha(0.5f))
                innerTextField()
            }
        )
    }
}

@Composable
private fun SetGroupCardButtons(
    modifier: Modifier = Modifier,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
) {
    Row(
        modifier.padding(horizontal = 8.dp),
    ) {
        TextButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.preferredWidth(8.dp))
            Text("Add Set")
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