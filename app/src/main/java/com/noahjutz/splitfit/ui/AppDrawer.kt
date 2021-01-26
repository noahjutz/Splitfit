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

package com.noahjutz.splitfit.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import com.noahjutz.splitfit.R

sealed class TopLevelScreens(
    val route: String,
    @StringRes val name: Int,
    val icon: ImageVector,
) {
    object Routines : TopLevelScreens("routines", R.string.tab_routines, Icons.Default.ViewAgenda)
    object Exercises : TopLevelScreens("exercises", R.string.tab_exercises, Icons.Default.SportsMma)
    object Workouts : TopLevelScreens("workouts", R.string.tab_workouts, Icons.Default.History)
}

val topLevelScreens = listOf(
    TopLevelScreens.Routines,
    TopLevelScreens.Exercises,
    TopLevelScreens.Workouts
)

@Composable
fun AppDrawer(
    navController: NavController,
    drawerState: DrawerState,
) {
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.padding(16.dp)) {
            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                Text(stringResource(R.string.app_name))
            }
        }

        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry.value?.arguments?.getString(KEY_ROUTE)
        for (screen in topLevelScreens) {
            DrawerButton(
                action = {
                    drawerState.close()
                    navController.popBackStack(navController.graph.startDestination, false)
                    navController.navigate(screen.route)
                },
                icon = screen.icon,
                label = stringResource(screen.name),
                isSelected = currentRoute == screen.route
            )
        }

        Divider(Modifier.padding(top = 8.dp))

        DrawerButton(
            action = {
                drawerState.close()
                navController.navigate("about")
            },
            icon = Icons.Default.Info,
            label = "About",
            isSelected = false
        )
        DrawerButton(
            action = {
                drawerState.close()
                navController.navigate("settings")
            },
            icon = Icons.Default.Settings,
            label = "Settings",
            isSelected = false
        )
    }
}

@Composable
private fun DrawerButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colors
    val imageAlpha = if (isSelected) {
        1f
    } else {
        0.6f
    }
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()
    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = icon,
                    colorFilter = ColorFilter.tint(textIconColor),
                    alpha = imageAlpha
                )
                Spacer(Modifier.preferredWidth(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}
