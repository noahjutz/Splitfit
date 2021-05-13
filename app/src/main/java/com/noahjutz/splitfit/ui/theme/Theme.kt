package com.noahjutz.splitfit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.noahjutz.splitfit.data.ColorTheme

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
    colors: ColorTheme,
    content: @Composable () -> Unit,
) {
    val colorTheme = when (colors) {
        ColorTheme.FollowSystem -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
        ColorTheme.Light -> LightColorPalette
        ColorTheme.Dark -> DarkColorPalette
        ColorTheme.Black -> BlackColorPalette
    }
    MaterialTheme(
        colors = colorTheme,
        content = content
    )
}