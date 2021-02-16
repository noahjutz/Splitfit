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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.util.DatastoreKeys
import com.noahjutz.splitfit.util.get
import kotlinx.coroutines.flow.map

sealed class TopLevelScreens(
    val route: String,
    @StringRes val name: Int,
    val icon: ImageVector? = null,
    @DrawableRes val iconRes: Int? = null,
) {
    object Routines : TopLevelScreens("routines", R.string.tab_routines, Icons.Default.ViewAgenda)
    object Exercises : TopLevelScreens("exercises", R.string.tab_exercises, iconRes = R.drawable.ic_dumbbell)
    object Workouts : TopLevelScreens("workouts", R.string.tab_workouts, Icons.Default.History)
    object Settings : TopLevelScreens("settings", R.string.tab_settings, Icons.Default.Settings)
}

val topLevelScreens = listOf(
    TopLevelScreens.Routines,
    TopLevelScreens.Exercises,
    TopLevelScreens.Workouts,
    TopLevelScreens.Settings,
)

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun SplitfitApp(
    preferences: DataStore<Preferences> = get(),
) {
    val navController = rememberNavController()

    val currentWorkoutId by preferences.data
        .map { it[DatastoreKeys.currentWorkout] }
        .collectAsState(initial = -1)
    val isWorkoutInProgress = currentWorkoutId?.let { it >= 0 } ?: false
    val isCurrentDestinationHomeTab = navController.currentBackStackEntryAsState()
        .value?.arguments?.getString(KEY_ROUTE) in topLevelScreens.map { it.route }
    val showWorkoutBottomSheet = isWorkoutInProgress && isCurrentDestinationHomeTab

    val navToWorkoutScreen = { navController.navigate("createWorkout?workoutId=$currentWorkoutId") }

    Scaffold(
        bottomBar = {
            Column {
                if (showWorkoutBottomSheet) WorkoutBottomSheet(navToWorkoutScreen)
                if (isCurrentDestinationHomeTab) HomeBottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            NavGraph(navController = navController)
        }
    }
}

@Composable
private fun HomeBottomBar(
    navController: NavController,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.arguments?.getString(KEY_ROUTE)
    BottomNavigation {
        for (screen in topLevelScreens) {
            BottomNavigationItem(
                icon = { Icon(screen.icon ?: vectorResource(screen.iconRes!!), null) },
                onClick = {
                    navController.popBackStack(navController.graph.startDestination, false)
                    if (screen.route != currentRoute) navController.navigate(screen.route)
                },
                label = { Text(stringResource(screen.name)) },
                selected = screen.route == currentRoute,
            )
        }
    }
}

@Composable
private fun WorkoutBottomSheet(navToWorkoutScreen: () -> Unit) {
    BottomAppBar(
        Modifier.clickable(onClick = navToWorkoutScreen),
        elevation = 4.dp,
        backgroundColor = colors.surface,
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.h6) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = "Workout in progress"
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = navToWorkoutScreen) {
            Icon(Icons.Default.ExpandLess, null)
        }
    }
}
