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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.Set
import java.util.*

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
                SetTableSetRow(
                    set = set,
                    logReps = logReps,
                    logWeight = logWeight,
                    logTime = logTime,
                    logDistance = logDistance,
                    showCheckbox = showCheckbox,
                )
                Divider()
            }
            SetTableRow {
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
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier.preferredHeight(52.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
        content = content,
    )
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
        if (logReps) SetTableCell(Modifier.weight(1f)) {
            var value by remember { mutableStateOf("") }
            TableCellTextField(value = value, onValueChange = { value = it })
        }
        if (logWeight) SetTableCell(Modifier.weight(1f)) {
            TableCellTextField(value = "", onValueChange = {})
        }
        if (logTime) SetTableCell(Modifier.weight(1f)) {
            TableCellTextField(value = "", onValueChange = {})
        }
        if (logDistance) SetTableCell(Modifier.weight(1f)) {
            TableCellTextField(value = "", onValueChange = {})
        }
    }
}

@Composable
private fun TableCellTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Box(Modifier.preferredHeight(48.dp), contentAlignment = Alignment.CenterStart) {
        AutoSelectTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(color = colors.onSurface),
            cursorColor = colors.onSurface,
            singleLine = true,
            decorationBox = { innerTextField ->
                if (value.isBlank()) Text("Hint", modifier = Modifier.alpha(0.5f))
                innerTextField()
            }
        )
    }
}

// TODO auto-select on tap
@Composable
private fun AutoSelectTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
    cursorColor: Color = LocalContentColor.current,
    maxLines: Int = Int.MAX_VALUE,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit = { it() },
    singleLine: Boolean = false,
) {
    val textFieldValue = remember(value) { TextFieldValue(value, TextRange(value.length)) }
    BasicTextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = { onValueChange(it.text) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        cursorColor = cursorColor,
        maxLines = maxLines,
        decorationBox = decorationBox,
        singleLine = singleLine,
    )
}