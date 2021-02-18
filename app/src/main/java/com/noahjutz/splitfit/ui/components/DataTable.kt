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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *  @sample TableSample
 */
@Composable
fun Table(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
    ) {
        Column(content = content)
    }
}

@Composable
fun ColumnScope.TableRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier.preferredHeight(52.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun ColumnScope.TableHeaderRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier.preferredHeight(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun RowScope.TableCell(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier, content = content)
}

@Preview
@Composable
fun TableSample() {
    MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
        Table {
            TableHeaderRow(Modifier.padding(start = 16.dp)) {
                TableCell(Modifier.weight(1f)) {
                    Text("H1", fontWeight = FontWeight.Bold)
                }
                TableCell(Modifier.weight(1f)) {
                    Text("H2", fontWeight = FontWeight.Bold)
                }
                TableCell(Modifier.weight(1f)) {
                    Text("H3", fontWeight = FontWeight.Bold)
                }
                TableCell {
                    Spacer(Modifier.preferredWidth(52.dp))
                }
            }
            Divider()
            TableRow(Modifier.padding(start = 16.dp)) {
                TableCell(Modifier.weight(1f)) {
                    Text("C1")
                }
                TableCell(Modifier.weight(1f)) {
                    Text("C2")
                }
                TableCell(Modifier.weight(1f)) {
                    Text("C3")
                }
                TableCell(Modifier.padding(end = 4.dp)) {
                    Checkbox(
                        modifier = Modifier.preferredWidth(48.dp),
                        checked = true,
                        onCheckedChange = {},
                    )
                }
            }
        }
    }
}