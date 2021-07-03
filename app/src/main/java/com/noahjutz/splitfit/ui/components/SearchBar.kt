package com.noahjutz.splitfit.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onClear = { onValueChange("") }
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = { Icon(Icons.Default.Search, null) },
        label = { Text("Search") },
        trailingIcon = (
            @Composable {
                IconButton(onClick = onClear) {
                    Icon(
                        Icons.Default.Clear,
                        null
                    )
                }
            }
            ).takeIf { value.isNotEmpty() }
    )
}

@ExperimentalAnimationApi
@Composable
@Preview(name = "Search bar light")
fun SearchBarPreview() {
    val (text, setText) = remember { mutableStateOf("") }
    MaterialTheme {
        SearchBar(
            text,
            setText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@ExperimentalAnimationApi
@Composable
@Preview(name = "Search bar dark")
fun SearchBarPreviewDark() {
    val (text, setText) = remember { mutableStateOf("") }
    MaterialTheme(colors = darkColors()) {
        SearchBar(
            text,
            setText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
