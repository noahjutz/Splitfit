package com.noahjutz.splitfit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.noahjutz.splitfit.ui.settings.Theme

@Composable
fun SplitfitTheme(
    colors: Theme,
    content: @Composable () -> Unit,
) {
    val colorTheme = when (colors) {
        Theme.FollowSystem -> if (isSystemInDarkTheme()) darkColors() else lightColors()
        Theme.Light -> lightColors()
        Theme.Dark -> darkColors()
        Theme.Black -> darkColors()
    }
    MaterialTheme(
        colors = colorTheme,
        content = content
    )
}