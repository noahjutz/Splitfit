package com.noahjutz.splitfit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    border: BorderStroke? = null,
) {
    val onClear = { onValueChange("") }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = typography.h6.copy(color = colors.onSurface),
        cursorBrush = SolidColor(colors.onSurface),
        singleLine = true
    ) { innerTextField ->
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(percent = 50),
            elevation = elevation,
            border = border,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(enabled = false, onClick = {}) { Icon(Icons.Default.Search, "Search") }
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text("Search", style = typography.h6, modifier = Modifier.alpha(0.5f))
                    }
                    innerTextField()
                }
                Spacer(Modifier.width(8.dp))
                AnimatedVisibility(
                    value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    IconButton(onClick = onClear) { Icon(Icons.Default.Clear, "Clear") }
                }
            }
        }
    }
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
