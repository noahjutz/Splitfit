package com.noahjutz.splitfit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.noahjutz.splitfit.ui.settings.Theme

private val DarkColorPalette = darkColors()

private val LightColorPalette = lightColors()

private val BlackColorPalette = DarkColorPalette

@Composable
fun SplitfitTheme(
    colors: Theme,
    content: @Composable () -> Unit,
) {
    val colorTheme = when (colors) {
        Theme.FollowSystem -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
        Theme.Light -> LightColorPalette
        Theme.Dark -> DarkColorPalette
        Theme.Black -> BlackColorPalette
    }
    MaterialTheme(
        colors = colorTheme,
        content = content
    )
}