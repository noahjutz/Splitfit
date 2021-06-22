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

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun SearchTopBar(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
) {
    DisposableEffect(Unit) {
        onDispose {
            onValueChange("")
        }
    }

    val searchFocusRequester = remember { FocusRequester() }
    var isInSearchMode by remember { mutableStateOf(false) }
    DisposableEffect(isInSearchMode) {
        if (isInSearchMode) searchFocusRequester.requestFocus()
        onDispose { }
    }
    if (isInSearchMode) {
        TopBar(
            title = {
                AppBarTextField(
                    modifier = Modifier.focusRequester(searchFocusRequester),
                    value = value,
                    onValueChange = onValueChange,
                    hint = "Search"
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onValueChange("")
                        isInSearchMode = false
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            },
        )
    } else {
        TopBar(
            title = { Text(title) },
            actions = {
                IconButton(onClick = { isInSearchMode = true }) {
                    Icon(Icons.Default.Search, "Search")
                }
            },
            navigationIcon = navigationIcon
        )
    }
}
