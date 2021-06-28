package com.noahjutz.splitfit.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    title: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title,
        modifier,
        navigationIcon,
        actions,
        elevation = 0.dp,
        backgroundColor = colors.surface
    )
}
