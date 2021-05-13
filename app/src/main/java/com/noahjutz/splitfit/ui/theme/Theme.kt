package com.noahjutz.splitfit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.noahjutz.splitfit.ui.settings.Theme

private val DarkColorPalette = darkColors(
    primary = PrimaryDesaturated,
    primaryVariant = PrimaryDark,
    secondary = Secondary,
    secondaryVariant = Secondary,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
)

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryDark,
    secondary = Secondary,
    secondaryVariant = SecondaryDark,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
)

private val BlackColorPalette = DarkColorPalette.copy(
    surface = Color.Black,
    background = Color.Black,
)

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